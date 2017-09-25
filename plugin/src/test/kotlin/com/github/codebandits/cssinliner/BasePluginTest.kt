package com.github.codebandits.cssinliner

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.extensions.TempDirectory
import org.junit.jupiter.extensions.TempDirectory.Root
import java.io.File
import java.nio.file.Path

@ExtendWith(TempDirectory::class)
abstract class BasePluginTest {

    private lateinit var projectRoot: File

    @BeforeEach
    fun setUp(@Root tempDirectory: Path) {
        projectRoot = tempDirectory.toFile()
    }

    protected fun withBuildScript(script: String, produceFile: (String) -> File = this::newFile): File =
            withBuildScriptIn(".", script, produceFile)

    private fun withBuildScriptIn(baseDir: String, script: String, produceFile: (String) -> File = this::newFile): File =
            withFile("$baseDir/build.gradle.kts", script, produceFile)

    protected fun withFile(fileName: String, text: String = "", produceFile: (String) -> File = this::newFile) =
            writeFile(produceFile(fileName), text)

    private fun writeFile(file: File, text: String): File =
            file.apply { writeText(text) }

    private fun newFile(fileName: String): File {
        makeParentFoldersOf(fileName)
        return File(projectRoot, fileName).canonicalFile.apply { createNewFile() }
    }

    private fun makeParentFoldersOf(fileName: String) =
            parentFileOf(fileName).mkdirs()

    private fun parentFileOf(fileName: String): File =
            File(projectRoot, fileName).parentFile
}
