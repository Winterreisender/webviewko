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

package com.github.winterreisender.webviewko

import com.sun.jna.Pointer

/**
 * The JVM binding to webview in Kotlin
 */

actual class WebviewKo actual constructor(debug: Int) {
    private val lib: WebviewJNA.WebviewLibrary = WebviewJNA.getLib()
    private val pWebview: Pointer = lib.webview_create(debug, Pointer.NULL)!!

    /**
     * Updates the title of the native window.
     *
     * Must be called from the UI thread.
     *
     * @param v the new title
     */
    actual fun title(v: String) = lib.webview_set_title(pWebview, v)

    /**
     * Navigates webview to the given URL
     *
     * URL may be a data URI, i.e. "data:text/text,...". It is often ok not to url-encode it properly, webview will re-encode it for you. Same as [navigate]
     *
     * @param v the URL or URI
     * */
    actual fun url(v: String) = lib.webview_navigate(pWebview, v)

    /**
     * Navigates webview to the given URL
     *
     * URL may be a data URI, i.e. "data:text/text,...". It is often ok not to url-encode it properly, webview will re-encode it for you. Same as [url]
     *
     * @param v the URL or URI
     * */
    actual fun navigate(v: String) = url(v)

    /**
     * Set webview HTML directly.
     *
     * @param v the HTML content
     */
    actual fun html(v :String) = lib.webview_set_html(pWebview, v)

    /**
     * Updates the size of the native window.
     *
     * Accepts a WEBVIEW_HINT
     *
     * @param hints can be one of `WEBVIEW_HINT_NONE`, `WEBVIEW_HINT_MIN`, `WEBVIEW_HINT_MAX` or `WEBVIEW_HINT_FIXED`
     */
    actual fun size(width: Int, height: Int, hints: WindowHint) =
        lib.webview_set_size(pWebview, width, height, hints.ordinal)


    /**
     * The window size hints used by `WebviewKo.size`
     *
     * A Wrapper of WEBVIEW_HINT_NONE, WEBVIEW_HINT_MIN, WEBVIEW_HINT_MAX and WEBVIEW_HINT_FIXED
     *
     */
    actual enum class WindowHint(v :Int) {
        None(WebviewJNA.WEBVIEW_HINT_NONE),
        Min(WebviewJNA.WEBVIEW_HINT_MIN),
        Max(WebviewJNA.WEBVIEW_HINT_MAX),
        Fixed(WebviewJNA.WEBVIEW_HINT_FIXED)
    }

    /**
     * Injects JS code at the initialization of the new page.
     *
     * Every time the webview will open a new page - this initialization code will be executed. It is guaranteed that code is executed before window.onload.
     *
     * @param js the JS code
     */
    actual fun init(js :String) = lib.webview_init(pWebview,js)

    /**
     * Evaluates arbitrary JS code.
     *
     * Evaluation happens asynchronously, also the result of the expression is ignored. Use the `webview_bind` function if you want to receive notifications about the results of the evaluation.
     *
     * @param js the JS code
     */
    actual fun eval(js :String) = lib.webview_eval(pWebview, js)

    // This does not work
    //actual fun <T,R> bind(name :String, fn :(T)-> R, x :Int) = lib.webview_bind(pWebview, name, object :WebviewLibrary.webview_bind_fn_callback {
    //    override actual fun apply(seq: String?, req: String?, arg: Pointer?) {
    //        val msg = Json.decodeFromString<T>(req!!)
    //        lib.webview_return(pWebview, seq, 0, Json.encodeToString(fn(req)))
    //    }
    //})

    /**
     * Binds a native Kotlin/Java callback so that it will appear under the given name as a global JS function.
     *
     * Callback receives a request string. Request string is a JSON array of all the arguments passed to the JS function.
     *
     * @param name the name of the global JS function
     * @param fn the callback function which receives the request parameter in JSON as input and return the response to JS in JSON. In Java the fn should be String response(WebviewKo webview, String request)
     */
    actual fun bindX(name :String, fn :WebviewKo.(String?)->Pair<String,Int>?) = lib.webview_bind(pWebview, name, object : WebviewJNA.WebviewLibrary.webview_bind_fn_callback {
        override fun apply(seq: String?, req: String?, arg: Pointer?) {
            val (response,status) = fn(req) ?: return
            println("$response,$status")
            lib.webview_return(pWebview, seq, status, response)
        }
    })

    // This is COOL but not good
    @JvmName("bindKt")
    actual inline fun <reified R : Any> bind(name :String, crossinline fn: WebviewKo.(String?) -> R) {
        //val isError = 1
        bindX(
            name
        ) { it ->
            when (R::class) {
                Pair::class   -> fn(it) as Pair<String, Int>
                Result::class -> (fn(it) as Result<String>    ).fold({Pair(it,0)}, {Pair(""" "$it" """,1)})
                String::class -> runCatching {fn(it) as String}.fold({Pair(it,0)}, {Pair(""" "$it" """,1)} )
                Unit::class   -> fn(it).let { null }
                Nothing::class-> runCatching{ fn(it) }.fold({error("Unexpected Behavior: fun (*)->Nothing runs successfully.")}, {Pair(""" "$it" """,1)})
                Any::class-> runCatching {fn(it) as String}.fold({Pair(it,0)}, {Pair(""" "$it" """,1)} )
                else -> throw IllegalArgumentException(R::class.simpleName)
            }
        }
    }

    // For Java Interop
    @JvmName("bind")
    fun bindJava(name :String, fn: WebviewKo.(String?) -> String) {
        bindX(name) {
            runCatching { fn(it) }.fold(
                { Pair(it, 0) },
                { Pair(""" "$it" """, 1) }
            )
        }
    }

    //@JvmName("__bindResult")
    // actual fun bind(name :String, fn: WebviewKo.(String?) -> Result<String>) {
    //     bindX(name) { arg ->
    //         fn(arg).fold(
    //             {Pair(it,0)},
    //             {Pair(""" "$it" """,1)}
    //         )
    //     }
    // }
    //
    // @JvmName("__bindUnit")
    // actual fun bind(name :String, fn: WebviewKo.(String?) -> Unit) {
    //     bindX(name) {
    //         fn(it)
    //         null
    //     }
    // }
    //
    // @JvmName("__bindPair")
    // actual fun bind(name :String, fn: WebviewKo.(String?) -> Pair<String, Int>) {
    //     bindX(name) {
    //         fn(it)
    //     }
    // }
    //
    // @JvmName("__bindAny")
    // actual fun bind(name :String, fn: WebviewKo.(String?) -> Any) {
    //     bindX(name) {
    //         runCatching { fn(it) as String }.fold(
    //             { Pair(it, 0) },
    //             { Pair(""" "$it" """, 1) }
    //         )
    //     }
    // }

    /**
     * Removes a callback that was previously set by `webview_bind`.
     *
     * @param name the name of JS function used in `webview_bind`
     */
    actual fun unbind(name: String) = lib.webview_unbind(pWebview, name)


    /**
     * Posts a function to be executed on the main thread.
     *
     * It safely schedules the callback to be run on the main thread on the next main loop iteration. Like `invokeLater` in Swing
     *
     * @param fn the function to be executed on the main thread.
     *
     */
    actual fun dispatch(fn : WebviewKo.()->Unit) = lib.webview_dispatch(pWebview,object : WebviewJNA.WebviewLibrary.webview_dispatch_fn_callback {
        override fun apply(webview: Pointer?, arg: Pointer?) {
            fn()
        }
    })


    /**
     * Runs the main loop and destroy it when terminated.
     *
     * This will block the thread.
     */
    actual fun show() {
        lib.webview_run(pWebview)
        lib.webview_destroy(pWebview)
    }

    /**
     * Stops the main loop.
     *
     * It is safe to call this function from another other background thread.
     *
     */
    actual fun terminate() = lib.webview_terminate(pWebview)

    /**
     * Return the Pointer of the webview.
     *
     * @return the JNA Pointer of the webview
     *
     */
    fun getWebviewPointer() = pWebview


}