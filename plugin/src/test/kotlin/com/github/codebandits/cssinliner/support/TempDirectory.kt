package com.github.codebandits.cssinliner.support

import org.junit.jupiter.api.extension.*
import org.junit.jupiter.api.extension.ExtensionContext.Namespace
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

class TempDirectory : AfterEachCallback, ParameterResolver {

    private val key = "tempDirectory"

    @Target(AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Root

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return parameterContext.parameter.isAnnotationPresent(Root::class.java) && parameterContext.parameter.type == Path::class.java
    }

    override fun resolveParameter(parameterContext: ParameterContext, context: ExtensionContext): Any {
        return getLocalStore(context).getOrComputeIfAbsent(key) { createTempDirectory(context) }
    }

    override fun afterEach(context: ExtensionContext) {
        val tempDirectory = getLocalStore(context).get(key) as Path?
        if (tempDirectory != null) {
            delete(tempDirectory)
        }
    }

    private fun getLocalStore(context: ExtensionContext): ExtensionContext.Store {
        return context.getStore(localNamespace(context))
    }

    private fun localNamespace(context: ExtensionContext): Namespace {
        return Namespace.create(TempDirectory::class.java, context)
    }

    private fun createTempDirectory(context: ExtensionContext): Path {
        try {
            val tempDirName: String = when {
                context.testMethod.isPresent -> context.testMethod.get().name
                context.testClass.isPresent -> context.testClass.get().name
                else -> context.displayName
            }
            return Files.createTempDirectory(tempDirName.replace("[^A-Za-z0-9]".toRegex(), "-"))
        } catch (e: IOException) {
            throw ParameterResolutionException("Could not create temp directory", e)
        }
    }

    private fun delete(tempDirectory: Path) {
        Files.walkFileTree(tempDirectory, object : SimpleFileVisitor<Path>() {

            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                return deleteAndContinue(file)
            }

            override fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {
                return deleteAndContinue(dir)
            }

            private fun deleteAndContinue(path: Path): FileVisitResult {
                Files.delete(path)
                return FileVisitResult.CONTINUE
            }
        })
    }
}
