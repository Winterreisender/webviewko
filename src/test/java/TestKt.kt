/*
 * Copyright (c) 2022  Winterreisender
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX short identifier: **Apache-2.0**
 */

import com.github.winterreisender.webviewko.*
import com.sun.jna.Pointer
import java.awt.*
import kotlin.test.Test

internal class TestKt {
    @Test fun `apiLayer simple`() {
        if (!Desktop.isDesktopSupported()) return

        with(WebviewKo()) {
            title("Title")
            size(800, 600)
            initJS("""console.log("Hello, from init")""")

            url("https://example.com")
            show()
        }
    }

    @Test fun `apiLayer full`() {
        if (!Desktop.isDesktopSupported()) return

        with(WebviewKo(1)) {
            title("Title")
            size(800,600)
            url("https://example.com")
            initJS("""console.log("Hello, from  init")""")

            bind("increment") {
                println("req: $it")
                val r :Int = Regex("""\["(\d+)"]""").find(it!!)!!.groupValues[1].toInt() + 1
                println(r)
                title(r.toString())
                if(r==8) {
                    url("https://example.com")
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

    @Test fun `apiLayer thread`() {
        Thread {
            Thread.currentThread().name = "JKDrcom Net Window"
            with(WebviewKo()) {
                title("JKDrcom Net Window")
                size(600, 500)
                url("https://example.com")
                show()
            }
        }.apply {
            start()
            join()
        }
    }

    @Test fun `jnaLayer bind`() {
        // This test implemented the bind.c in webview

        if (!Desktop.isDesktopSupported()) return

        val webviewNative = WebviewJNA.getInstance()

        with(webviewNative) {
            val pWebview = webview_create(1, Pointer.NULL)
            webview_set_title(pWebview, "Hello")
            webview_set_size(pWebview, 800, 600, WebviewJNA.WEBVIEW_HINT_NONE)

            webview_init(pWebview, """console.log("Hello, from init")""")

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

            webview_eval(pWebview, """console.log("Hello, from  eval")""")

            webview_run(pWebview)
            webview_destroy(pWebview)
        }
    }

}
