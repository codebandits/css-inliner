package com.github.codebandits.cssinliner

import com.github.codebandits.cssinliner.support.BasePluginTest
import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("The com.github.codebandits.css-inliner Gradle plugin")
class CssInlinerPluginTest : BasePluginTest() {

    @BeforeEach
    fun setUp() {
        withFile("src/main/resources/style.css", """
            .fancy {
                font-style: italic;
            }
        """.trimIndent())

        withFile("src/main/resources/home.html", """
            <html>
            <body>
            <div class="fancy">Welcome</div>
            </body>
            </html>
        """.trimIndent())
    }

    @Test
    fun `should work in a Kotlin project built with a Kotlin Gradle buildscript`() {
        withFile("build.gradle.kts", """
            import com.github.codebandits.cssinliner.CssInlinerPluginExtension

            buildscript {
                repositories {
                    maven { setUrl("$testRepository") }
                    mavenCentral()
                }
                dependencies {
                    classpath("com.github.codebandits.css-inliner:com.github.codebandits.css-inliner.gradle.plugin:$version")
                }
            }

            plugins {
                kotlin("jvm") version "1.2.10"
            }

            apply {
                plugin("com.github.codebandits.css-inliner")
            }

            configure<CssInlinerPluginExtension> {
                cssFile = project.file("src/main/resources/style.css")
            }
        """.trimIndent())

        val result: BuildResult = build("processResources")

        assertThat(result.outcomeOf(":processResources")).isEqualTo(TaskOutcome.SUCCESS)

        val processedHtml = getFile("build/resources/main/home.html")
        assertThat(processedHtml).doesNotContain("""<div class="fancy">""")
        assertThat(processedHtml).contains("""<div style="font-style: italic;">""")
    }

    @Test
    fun `should work in a Java project built with a Kotlin Gradle buildscript`() {
        withFile("build.gradle.kts", """
            import com.github.codebandits.cssinliner.CssInlinerPluginExtension

            buildscript {
                repositories {
                    maven { setUrl("$testRepository") }
                    mavenCentral()
                }
                dependencies {
                    classpath("com.github.codebandits.css-inliner:com.github.codebandits.css-inliner.gradle.plugin:$version")
                }
            }

            plugins {
                java
            }

            apply {
                plugin("com.github.codebandits.css-inliner")
            }

            configure<CssInlinerPluginExtension> {
                cssFile = project.file("src/main/resources/style.css")
            }
        """.trimIndent())

        val result: BuildResult = build("processResources")

        assertThat(result.outcomeOf(":processResources")).isEqualTo(TaskOutcome.SUCCESS)

        val processedHtml = getFile("build/resources/main/home.html")
        assertThat(processedHtml).doesNotContain("""<div class="fancy">""")
        assertThat(processedHtml).contains("""<div style="font-style: italic;">""")
    }

    @Test
    fun `should work in a Java project built with a Groovy Gradle buildscript`() {
        withFile("build.gradle", """
            buildscript {
                repositories {
                    maven { setUrl('$testRepository') }
                    mavenCentral()
                }
                dependencies {
                    classpath 'com.github.codebandits.css-inliner:com.github.codebandits.css-inliner.gradle.plugin:$version'
                }
            }

            plugins {
                id 'java'
            }

            apply plugin: 'com.github.codebandits.css-inliner'

            'css-inliner' {
                cssFile = project.file('src/main/resources/style.css')
            }
        """.trimIndent())

        val result: BuildResult = build("processResources")

        assertThat(result.outcomeOf(":processResources")).isEqualTo(TaskOutcome.SUCCESS)

        val processedHtml = getFile("build/resources/main/home.html")
        assertThat(processedHtml).doesNotContain("""<div class="fancy">""")
        assertThat(processedHtml).contains("""<div style="font-style: italic;">""")
    }

    @Test
    fun `should fail when the cssFile configuration property is not set`() {
        withFile("build.gradle.kts", """
            import com.github.codebandits.cssinliner.CssInlinerPluginExtension

            buildscript {
                repositories {
                    maven { setUrl("$testRepository") }
                    mavenCentral()
                }
                dependencies {
                    classpath("com.github.codebandits.css-inliner:com.github.codebandits.css-inliner.gradle.plugin:$version")
                }
            }

            plugins {
                java
            }

            apply {
                plugin("com.github.codebandits.css-inliner")
            }
        """.trimIndent())

        val result: BuildResult = buildAndFail("processResources")

        assertThat(result.outcomeOf(":processResources")).isEqualTo(TaskOutcome.FAILED)
        assertThat(result.output).contains("The cssFile configuration property must be set")
    }

    @Test
    fun `should allow filesMatching to be configured`() {
        withFile("build.gradle", """
            buildscript {
                repositories {
                    maven { setUrl('$testRepository') }
                    mavenCentral()
                }
                dependencies {
                    classpath 'com.github.codebandits.css-inliner:com.github.codebandits.css-inliner.gradle.plugin:$version'
                }
            }

            plugins {
                id 'java'
            }

            apply {
                plugin 'com.github.codebandits.css-inliner'
            }

            'css-inliner' {
                cssFile = project.file('src/main/resources/style.css')
                filesMatching = ['**/*.ftl']
            }
        """.trimIndent())

        withFile("src/main/resources/people.ftl", """
            [#list people as person]
            <div class="fancy">${'$'}{person.name}</div>
            [/#list]
        """.trimIndent())

        val result: BuildResult = build("processResources")

        assertThat(result.outcomeOf(":processResources")).isEqualTo(TaskOutcome.SUCCESS)

        val processedHtml = getFile("build/resources/main/people.ftl")
        assertThat(processedHtml).doesNotContain("""<div class="fancy">""")
        assertThat(processedHtml).contains("""<div style="font-style: italic;">""")
    }

    @Test
    fun `should ignore files that are not configured to be processed`() {
        withFile("build.gradle.kts", """
            import com.github.codebandits.cssinliner.CssInlinerPluginExtension

            buildscript {
                repositories {
                    maven { setUrl("$testRepository") }
                    mavenCentral()
                }
                dependencies {
                    classpath("com.github.codebandits.css-inliner:com.github.codebandits.css-inliner.gradle.plugin:$version")
                }
            }

            plugins {
                kotlin("jvm") version "1.2.10"
            }

            apply {
                plugin("com.github.codebandits.css-inliner")
            }

            configure<CssInlinerPluginExtension> {
                cssFile = project.file("src/main/resources/style.css")
            }
        """.trimIndent())

        withFile("src/main/resources/bye", """
            <div class="fancy">Goodbye</div>
        """.trimIndent())

        val result: BuildResult = build("processResources")

        assertThat(result.outcomeOf(":processResources")).isEqualTo(TaskOutcome.SUCCESS)

        val processedHtml = getFile("build/resources/main/bye")
        assertThat(processedHtml).contains("""<div class="fancy">""")
        assertThat(processedHtml).doesNotContain("""<div style="font-style: italic;">""")
    }
}
