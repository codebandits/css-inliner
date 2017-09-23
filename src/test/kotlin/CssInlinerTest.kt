import org.assertj.core.api.Assertions.assertThat
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser
import org.junit.jupiter.api.Test

class CssInlinerTest {

    @Test
    fun `should inline a single css rule`() {
        val css = """
            h1 {
                background-color: blue;
            }
        """.trimIndent()

        val html = """
            <!DOCTYPE html>
            <html lang="en">
            <body>
            <h1>hai</h1>
            </body>
            </html>
        """.trimIndent()

        val subject = CssInliner()

        val result: String = subject.inlineCss(html, css)

        val resultStyle = result.document
                .getElementsByTag("h1").first()
                .attr("style")

        assertThat(resultStyle).isEqualTo("background-color: blue;")
    }

    @Test
    fun `should inline multiple css rules to the same element`() {
        val css = """
            h1 {
                font-family: monospace;
            }
            .strong {
                font-weight: 700;
            }
        """.trimIndent()

        val html = """
            <!DOCTYPE html>
            <html lang="en">
            <body>
            <h1 class="strong">hai</h1>
            </body>
            </html>
        """.trimIndent()

        val subject = CssInliner()

        val result: String = subject.inlineCss(html, css)

        val resultStyle = result.document
                .getElementsByTag("h1").first()
                .attr("style")

        assertThat(resultStyle).isEqualTo("font-family: monospace; font-weight: 700;")
    }

    @Test
    fun `should preserve html fragments`() {
        val html = "<h1>hai</h1>"

        val subject = CssInliner()

        val result: String = subject.inlineCss(html, "")

        assertThat(result.document.getElementsByTag("html")).hasSize(0)
    }

    @Test
    fun `should strip class attributes from elements`() {
        val html = """
            <!DOCTYPE html>
            <html lang="en">
            <body>
            <h1 class="strong">hai</h1>
            </body>
            </html>
        """.trimIndent()

        val subject = CssInliner()

        val result: String = subject.inlineCss(html, "")

        val classNames = result.document
                .getElementsByTag("h1").first()
                .classNames()

        assertThat(classNames).isEmpty()
    }
}

private val String.document: Document
    get() = Jsoup.parse(this, "", Parser.xmlParser())
