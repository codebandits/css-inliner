import com.steadystate.css.parser.CSSOMParser
import com.steadystate.css.parser.SACParserCSS3
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import org.w3c.css.sac.InputSource
import org.w3c.dom.css.*
import java.io.StringReader

class CssInliner {

    private val cssParser = CSSOMParser(SACParserCSS3())

    fun inlineCss(html: String, css: String): String {
        val styleSheet: CSSStyleSheet = cssParser.parseStyleSheet(InputSource(StringReader(css)), null, null)
        val document: Document = Jsoup.parse(html, "", Parser.xmlParser())
        return inlineCss(document, styleSheet).outerHtml()
    }

    private fun inlineCss(document: Document, styleSheet: CSSStyleSheet): Document {
        styleSheet.cssRules.toList().forEach { rule ->
            when (rule) {
                is CSSStyleRule -> {
                    document.select(rule.selectorText).forEach { element: Element ->
                        rule.style.toList().forEach { addStyleToElement(it, element) }
                    }
                }
            }
        }
        document.allElements.forEach { it.removeAttr("class") }
        return document
    }
}

private fun CSSRuleList.toList(): List<CSSRule> {
    return IntRange(0, this.length - 1).map(this::item)
}

private fun CSSStyleDeclaration.toList(): List<CssStyle> {
    return IntRange(0, this.length - 1).map {
        val propertyName = this.item(it)
        val propertyValue = this.getPropertyValue(propertyName)
        CssStyle(propertyName, propertyValue)
    }
}

private data class CssStyle(
        val propertyName: String,
        val propertyValue: String
)

private fun addStyleToElement(cssStyle: CssStyle, element: Element) {
    val existingStyle = element.attr("style")
    val styleString = "$existingStyle ${cssStyle.propertyName}: ${cssStyle.propertyValue};".trim()
    element.attr("style", styleString)
}
