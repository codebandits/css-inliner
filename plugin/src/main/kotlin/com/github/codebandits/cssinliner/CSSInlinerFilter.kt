package com.github.codebandits.cssinliner

import java.io.FilterReader
import java.io.Reader
import java.io.StringReader

internal class CSSInlinerFilter(`in`: Reader) : FilterReader(`in`) {

    fun setCss(css: String) {
        val processedHtml = CssInliner().inlineCss(`in`.readText(), css)
        `in` = StringReader(processedHtml)
    }
}
