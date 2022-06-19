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
import com.github.winterreisender.webviewko.WebviewJNA.WebviewLibrary
import com.sun.jna.Pointer
import java.awt.*
import kotlin.test.Test
import kotlinx.serialization.json.*

internal class TestKt {
    @Test fun `apiLayer simple`() {
        if (!Desktop.isDesktopSupported()) return

        with(WebviewKo()) {
            title("Title")
            size(800, 600)

            url("https://example.com")
            show()
        }
    }

    @Test fun `webview example basic`() {
        // This tests the example from webview: https://github.com/webview/webview/blob/master/examples/basic.cc
        if (!Desktop.isDesktopSupported()) return
        with(WebviewKo()) {
            title("Basic Example")
            size(480, 320, WindowHint.None)
            html("Thanks for using webview!")
            show()
        }
    }
    @Test fun `api Full`() {
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
                    terminate()
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
        if (!Desktop.isDesktopSupported()) return

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
        // This test implemented a similar bind.c example in webview
        if (!Desktop.isDesktopSupported()) return

        with(WebviewJNA.getLib()) {
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

    @Test fun `json Test`() {
        // Example about using third part Json Serialization (kotlinx-serialization-json)
        if (!Desktop.isDesktopSupported()) return

        with(WebviewKo(1)) {
            title("Title")
            size(800,600)
            url("https://example.com")
            initJS("""console.log("Hello, from  init")""")

            bind("increment") {
                // [7, {count: 2, max 8}]
                val json = Json.parseToJsonElement(it!!)
                val arg1 = json.jsonArray[0].jsonPrimitive.float
                val count = json.jsonArray[1].jsonObject["count"]!!.jsonPrimitive.int
                val max = json.jsonArray[1].jsonObject["max"]!!.jsonPrimitive.int

                println("$json $arg1 $count $max")

                buildJsonObject {
                    put("count", arrayOf(count+1,max).min())
                }.toString()
            }

            html("""
                <button id="increment">Tap me</button>
                <div>You tapped <span id="count">0</span> time(s).</div>
                <script>
                  const [incrementElement, countElement] = document.querySelectorAll("#increment, #count");
                  document.addEventListener("DOMContentLoaded", () => {
                    incrementElement.addEventListener("click", () => {
                      window.increment(7.2,{count: parseInt(countElement.innerText), max: 8}).then(result => {
                        countElement.textContent = result.count;
                      });
                    });
                  });
                </script>
            """.trimIndent())

            show()
        }
    }

}
