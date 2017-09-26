package com.github.codebandits.cssinliner

import java.io.File

open class CssInlinerPluginExtension(
        var cssFile: File? = null,
        var filesMatching: MutableList<String> = mutableListOf("**/*.html")
)
