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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.*
import kotlinx.coroutines.*
import kotlin.test.Test


internal class TestKt {

    @Test fun `apiLayer simple`() {
        with(WebviewKo()) {
            title("Title")
            size(800, 600)
            url("https://example.com")
            show()
            println("Goodbye webview")
        }
    }

    @Test fun `webview example basic`() {
        // This tests the example from webview: https://github.com/webview/webview/blob/master/examples/basic.cc

        with(WebviewKo()) {
            title("Basic Example")
            size(480, 320, WebviewKo.WindowHint.None)
            html("Thanks for using webview!")
            show()
        }
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


    @Test fun `json Test`() {
        // Example about using third part Json Serialization (kotlinx-serialization-json)


        with(WebviewKo(1)) {
            title("Title")
            size(800,600)
            url("https://example.com")
            init("""console.log("Hello, from  init")""")

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
