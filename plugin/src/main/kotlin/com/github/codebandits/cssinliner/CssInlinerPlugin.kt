package com.github.codebandits.cssinliner

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke
import org.gradle.language.jvm.tasks.ProcessResources

open class CssInlinerPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        val extension = target.extensions.create("css-inliner", CssInlinerPluginExtension::class.java)

        target.run {
            tasks {
                "processResources"(ProcessResources::class) {
                    filesMatching(extension.filesMatching) {
                        filter(mapOf("cssFile" to extension.cssFile), CSSInlinerFilter::class.java)
                    }
                }
            }
        }
//        target.tasks {
//            "processResources"(ProcessResources::class) {
//                filesMatching("**/*.yaml") {
//                    filter(com.github.codebandits.cssinliner.CSSInlinerFilter::class)
//                }
//            }
//        }
    }
}
