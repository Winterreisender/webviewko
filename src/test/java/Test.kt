import com.github.winterreisender.webviewko.WebviewJNA
import com.github.winterreisender.webviewko.WebviewJNA.getRawInstance
import com.github.winterreisender.webviewko.WebviewKo
import com.github.winterreisender.webviewko.WebviewLibrary
import com.github.winterreisender.webviewko.WindowHint
import com.sun.jna.Pointer
import java.awt.Desktop
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class Test {
    @Test fun `apiLayer test`() {
        if (!Desktop.isDesktopSupported()) return
        val webview = WebviewKo().apply {
            title = "webviewKo Test"
            size = Pair(1024,768)
            uri = URI("https://www.whatsmybrowser.org/")
            windowHint = WindowHint.None
        }
        webview.show()
    }
    @Test fun `jnaLayer bind`() {
        // This test implemented the bind.c in webview

        if (!Desktop.isDesktopSupported()) return

        // use getInstance to copy dll automatically
        val webviewNative = getRawInstance()

        with(webviewNative) {
            val pWebview = webview_create(1, Pointer.NULL)
            webview_set_title(pWebview, "Hello")
            webview_set_size(pWebview, 800, 600, WebviewJNA.WEBVIEW_HINT_NONE)

            webview_init(pWebview, """console.log("Hello, from jnaLayerTest2 init")""")

            val html = """
                <button id="increment">Tap me</button>
                <div>You tapped <span id="count">0</span> time(s).</div>
                <script>
                  const [incrementElement, countElement] = document.querySelectorAll("#increment, #count");
                  document.addEventListener("DOMContentLoaded", () => {
                    incrementElement.addEventListener("click", () => {
                      window.increment(countElement.innerText).then(result => {
                        countElement.textContent = result.count;
                      });
                    });
                  });
                </script>
            """.trimIndent()

            val callback = object :WebviewLibrary.webview_bind_fn_callback {
                override fun apply(seq: String?, req: String?, arg: Pointer?) {
                    println("seq: $seq")
                    println("req: $req")
                    val r :Int = Regex("""\["(\d+)"]""").find(req!!)!!.groupValues[1].toInt() + 1
                    println(r)
                    webview_return(pWebview, seq, 0, "{count: $r}")
                }

            }
            webview_bind(pWebview,"increment",callback)
            webview_set_html(pWebview, html);

            webview_eval(pWebview, """console.log("Hello, from jnaLayerTest2 eval")""")

            webview_run(pWebview)
            webview_destroy(pWebview)
        }
    }

}
