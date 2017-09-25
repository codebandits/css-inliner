package com.github.codebandits.cssinliner

import java.io.File

open class CssInlinerPluginExtension(
        var cssFile: File? = null,
        val filesMatching: MutableList<String> = mutableListOf("**/*.html", "**/*.ftl")
)
