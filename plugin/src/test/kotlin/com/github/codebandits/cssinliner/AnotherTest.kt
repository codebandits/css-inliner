package com.github.codebandits.cssinliner

import org.junit.jupiter.api.Test

class AnotherTest : BasePluginTest() {

    @Test
    fun `new setup`() {
        withBuildScript("""
            plugins {
                `java-gradle-plugin`
                `kotlin-dsl`
            }

            repositories {
                jcenter()
            }

            dependencies {
                testCompile("junit:junit:4.12")
            }
        """.trimIndent())

        withFile("src/main/kotlin/code.kt", """
            import org.gradle.api.Plugin
            import org.gradle.api.Project
            import org.gradle.kotlin.dsl.embeddedKotlinVersion
            class MyPlugin : Plugin<Project> {
                override fun apply(project: Project) {
                    project.run {
                        println("Plugin Using Embedded Kotlin " + embeddedKotlinVersion)
                    }
                }
            }
        """)
    }
}

