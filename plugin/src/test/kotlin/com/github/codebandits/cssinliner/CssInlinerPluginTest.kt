package com.github.codebandits.cssinliner

import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test

class CssInlinerPluginTest {

    private val project = ProjectBuilder.builder().build()

    @Test
    fun `should not fail when applied`() {
        project.apply { it.plugin("com.github.codebandits.css-inliner") }
    }
}
