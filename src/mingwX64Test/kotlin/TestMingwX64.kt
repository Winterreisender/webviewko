/*
 * Copyright (C) 2022. Winterreisender
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX short identifier: Apache-2.0
 */

import com.github.winterreisender.webviewko.WebviewKo
import com.github.winterreisender.cwebview.*
import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.Worker
import kotlin.test.Test


class TestMingwX64 {
    @Test
    fun test0() {
        val w : webview_t = webview_create(0,null)!!
        webview_set_size(w, 800, 600, 0)
        webview_navigate(w, "https://example.com")
        webview_run(w)
        webview_destroy(w)
    }

    @Test fun `api Full`() {
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
                        eval("alert('Hello from dispatch 8')")
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

    @Test fun `multi windows`() {
        val webviewKo1 = WebviewKo().apply {
            title("1")
            size(900, 500)
            url("https://example.com")
        }

        val w1 = Worker.start().execute(TransferMode.SAFE, {webviewKo1}) { it ->
            it.dispatch {
                it.title("Hello From Thread")
            }
        }

        webviewKo1.show()
        w1.consume {  }
    }


}