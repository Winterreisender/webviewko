import com.github.winterreisender.webviewko.WebviewKo
import com.github.winterreisender.cwebview.*
import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.Worker
import kotlin.native.concurrent.freeze
import kotlin.test.Ignore
import kotlin.test.Test


class TestNative {
    @Ignore
    fun test0() {
        val w : webview_t? = webview_create(0,null)
        webview_set_size(w, 800, 600, 0)
        webview_navigate(w, "https://example.com")
        webview_run(w)
        webview_destroy(w)
    }

    @Ignore fun `api Full`() {
        with(WebviewKo(1)) {
            title("Title")
            size(800,600)
            url("https://example.com")
            init("""console.log("Hello, from  init")""")

            bind("increment") {
                println("req: $it")
                val r :Int = Regex("""\["(\d+)"]""").find(it!!)!!.groupValues[1].toInt() + 1
                println(r)
                title(r.toString())
                if(r==8) {
                    dispatch {
                        title("8")
                        //eval("""alert('Hello')""")
                        //terminate()
                    }
                }
                "{count: $r}"
            }

            html("""
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
            """.trimIndent())

            show()
        }
    }

    @Ignore fun `multi windows`() {
        val webviewKo1 = WebviewKo().apply {
            title("1")
            size(900, 500)
            url("https://example.com")
        }

        webviewKo1.freeze()

        val w1 = Worker.start().execute(TransferMode.SAFE, {webviewKo1}) { it ->
            it.dispatch {
                it.title("Hello From Thread")
            }
        }

        webviewKo1.show()
        w1.consume {  }
    }


}