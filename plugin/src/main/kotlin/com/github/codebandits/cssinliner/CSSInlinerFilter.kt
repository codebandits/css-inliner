package com.github.codebandits.cssinliner

import java.io.File
import java.io.FilterReader
import java.io.Reader
import java.io.StringReader

internal class CSSInlinerFilter(properties: Map<String, Any>, `in`: Reader) : FilterReader(StringReader(inlineCSS(properties, `in`.readText()))) {

    companion object {
        private fun inlineCSS(properties: Map<String, Any>, input: String): String {
            val css = (properties["css"] as File).readText()
            return CssInliner().inlineCss(input, css)
        }
    }
}
