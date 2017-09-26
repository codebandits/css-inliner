package com.github.codebandits.cssinliner

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke
import org.gradle.language.jvm.tasks.ProcessResources

open class CssInlinerPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        val extension = target.extensions.create("css-inliner", CssInlinerPluginExtension::class.java)

        target.run {
            afterEvaluate {
                tasks {
                    "processResources"(ProcessResources::class) {
                        filesMatching(extension.filesMatching) {
                            val css = extension.cssFile?.readText() ?: throw CssFileMissing()
                            filter(mapOf("css" to css), CSSInlinerFilter::class.java)
                        }
                    }
                }
            }
        }
    }

    class CssFileMissing : RuntimeException("The cssFile configuration property must be set")
}
