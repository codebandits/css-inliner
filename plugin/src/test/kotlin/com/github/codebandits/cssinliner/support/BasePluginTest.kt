package com.github.codebandits.cssinliner.support

import com.github.codebandits.cssinliner.support.TempDirectory.Root
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.opentest4j.IncompleteExecutionException
import java.io.File
import java.nio.file.Path
import java.util.*

@ExtendWith(TempDirectory::class)
open class BasePluginTest {

    @BeforeEach
    fun setUp(@Root tempDirectory: Path) {
        projectRoot = tempDirectory.toFile()
    }

    private lateinit var projectRoot: File

    private val testProperties: Properties =
            javaClass.getResourceAsStream("/test.properties")?.use { Properties().apply { load(it) } }
                    ?: throw IncompleteExecutionException("must supply test.properties (run tests with Gradle)")

    protected val version =
            testProperties["version"]?.toString()
                    ?: throw IncompleteExecutionException("must set version in test.properties")

    protected val testRepository: String get() = File("../build/repository").canonicalPath

    protected fun getFile(fileName: String): String = File(projectRoot, fileName).readText()

    protected fun withFile(fileName: String, text: String): File {
        val file = File(projectRoot, fileName).canonicalFile
        return file
                .apply { parentFile.mkdirs() }
                .apply { createNewFile() }
                .apply { writeText(text) }
    }

    protected fun BuildResult.outcomeOf(taskPath: String): TaskOutcome {
        val task = task(taskPath) ?: throw IncompleteExecutionException("task \"$taskPath\" not found")
        return task.outcome
    }

    protected fun build(vararg arguments: String) = gradleRunnerFor(projectRoot, *arguments).build()
    protected fun buildAndFail(vararg arguments: String) = gradleRunnerFor(projectRoot, *arguments).buildAndFail()

    private fun gradleRunnerFor(projectDir: File, vararg arguments: String): GradleRunner = GradleRunner.create().run {
        withProjectDir(projectDir)
        withArguments(*arguments, "--stacktrace")
        return this
    }
}
