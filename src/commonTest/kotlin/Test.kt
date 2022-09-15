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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.*
import kotlin.test.Test

internal class Test {
    // A simple test showed in README
    @Test fun demo_simple() {
        WebviewKo().run {
            title("Title")
            size(800, 600)
            url("https://example.com")
            start()
        }
    }

    // An interactive test showed in README
    @Test fun demo_interact() {
        WebviewKo(1).run {
            title("Test")
            init("""console.log("Hello, from init")""")
            bind("increment") {
                val r :Int = it.removePrefix("[\"").removeSuffix("\"]").toInt() + 1
                println(r.toString())
                if(r==8)
                    terminate()
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
                      });});});
                 </script>""")
                start()
        }
    }

    @Test fun api_Full() {
        WebviewKo(1).run {
            title("Title")
            size(800,600)
            url("https://example.com")
            init("""console.log("Hello, from  init")""")

            bind("incrementKt") {
                println("req: $it")
                val r :Int = Regex("""\["(\d+)"]""").find(it)!!.groupValues[1].toInt() + 1
                println(r)
                title(r.toString())
                if(r==8) {
                    terminate()
                }
                "{count: $r}"
            }

            bind("ktExceptionTest") {
                throw NotImplementedError() // Should crash
            }

            bind("jsRejectTest") {
                throw WebviewKo.JSRejectException("NotImplemented") // Should call `Promise.reject(reason :string)` and Get an Exception in JS
            }

            bind("jsRejectTest2") {
                // Should call `Promise.reject(reason :string)` and Get an Exception in JS
                throw WebviewKo.JSRejectException(json = """ 
                    {
                        "jsonrpc": "2.0",
                        "code" : 1,
                        "message" : "Nothing found",
                        "data":null
                    }
                """.trimIndent())
            }

            html("""
                <button id="increment">Tap me</button>
                <div>You tapped <span id="count">0</span> time(s).</div>
                <script>
                  const [incrementElement, countElement] = document.querySelectorAll("#increment, #count");
                  document.addEventListener("DOMContentLoaded", () => {
                    incrementElement.addEventListener("click", () => {
                      window.incrementKt(countElement.innerText).then(result => {
                        countElement.textContent = result.count;
                      });
                    });
                  });
                </script>
            """.trimIndent())

            start()
        }
    }


    @Test fun jsonTest() {
        // Example about using third part Json Serialization (kotlinx-serialization-json)
        WebviewKo(1).run {
            title("Title")
            size(800,600)
            url("https://example.com")
            init("""console.log("Hello, from  init")""")

            bind("increment") {
                // [7, {count: 2, max 8}]
                val json = Json.parseToJsonElement(it)
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
            start()
        }
    }

    @Test fun fullscreen() {
        WebviewKo().run {
            size(800,600)
            init("""
                let w = window.open('');
                w.document.write(`
                // You HTML Content here
                <html>
                <body>
                	<button onclick="document.querySelector('body').requestFullscreen()"> fullscreen </button>
                </body>
                </html>
                `);
            """.trimIndent())
            navigate("about:blank")
            start()
        }
    }

    @Test fun reopen0() {
        WebviewKo().run {
            size(800,600)
            navigate("about:blank")
            start()
        }
        WebviewKo().run {
            size(800,600)
            navigate("about:blank")
            start()
        }
        WebviewKo().run {
            size(800,600)
            navigate("about:blank")
            start()
        }
    }

}
