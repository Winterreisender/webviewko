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
import com.sun.jna.ptr.PointerByReference

/**
 * The Kotlin/JVM binding to webview
 *
 * @constructor create a webview or throws `Exception` if failed
 */

actual class WebviewKo  {
    private var lib: WebviewJNA.WebviewLibrary
    private var pWebview: Pointer

    constructor(debug: Int, libPath :String?, target: PointerByReference?) {
        lib = if(libPath==null) WebviewJNA.getLib() else WebviewJNA.getLib(libPath)
        pWebview = lib.webview_create(debug, target) ?: throw Exception("Failed to create webview")
    }

    actual constructor(debug: Int, libPath :String?) :this(debug, libPath,null)


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
     * @param url the URL or URI
     * */
    actual fun navigate(url: String) = url(url)

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
     * @param hints can be one of [WindowHint]
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

    /**
     * Binds a native Kotlin/Java callback so that it will appear under the given name as a global JS function.
     *
     * Callback receives a request string. Request string is a JSON array of all the arguments passed to the JS function.
     *
     * @param name the name of the global JS function
     * @param fn the callback function which receives the request parameter in JSON as input and return the response to JS in JSON. In Java the fn should be String response(WebviewKo webview, String request)
     */
    actual fun bindRaw(name :String, fn :WebviewKo.(String?)->Pair<String,Int>?) = lib.webview_bind(pWebview, name, object : WebviewJNA.WebviewLibrary.webview_bind_fn_callback {
        override fun apply(seq: String?, req: String?, arg: Pointer?) {
            val (response,status) = fn(req) ?: return
            lib.webview_return(pWebview, seq, status, response)
        }
    })

    /**
     * Should be used in [bind] to throw an exception in JS
     *
     * This exception will be caught by [bind] and trigger the `Promise.reject(reason)` in JS.
     *
     * @param reason the reason shown in JS.
     * @param json the JSON Exception object for JS. If it's not null, `reason` willed be covered
     */
    actual class JSRejectException actual constructor(reason: String?, json :String?) : Throwable(json ?: """ "$reason" """)


    /**
     * Binds a Kotlin callback so that it will appear under the given name as a global JS function.
     *
     * @param name the name of the global JS function
     * @param fn the callback function which receives the request parameter in JSON as input and return the response JSON. If you want to reject the `Promise`, throw [JSRejectException] in `fn`
     */
    actual fun bind(name :String, fn: WebviewKo.(String) -> String) {
        bindRaw(name) {
            kotlin.runCatching { fn(it ?: "") }.fold(
                onSuccess = { Pair(it, 0) },
                onFailure =  {
                    when(it) {
                        is JSRejectException -> Pair(it.message ?: "", 1)
                        else -> throw it
                    }
                }
            )
        }
    }


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
     * Runs the main loop until it's terminated. **After this function exits - you must destroy the webview**.
     *
     * This will block the thread.
     */
    actual fun start() = lib.webview_run(pWebview)

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
     * @return the JNA [Pointer] of the webview
     *
     */
    fun getWebviewPointer() = pWebview

    /**
     * Destroy the webview and close the native window.
     *
     * You must destroy the webview after [start]
     *
     */
    actual fun destroy() {
        lib.webview_destroy(pWebview)
    }

    /**
     * Runs the main loop until it's terminated and destroy the webview after that.
     *
     * This will block the thread. This is the same as calling [start] and [destroy] serially
     */
    actual fun show() {
        lib.webview_run(pWebview)
        lib.webview_destroy(pWebview)
    }




}

// This is COOL but not good
//@JvmName("bindKt")
//actual inline fun <reified R : Any> bindEx(name :String, crossinline fn: WebviewKo.(String?) -> R) {
//    //val isError = 1
//    bindRaw(
//        name
//    ) { it ->
//        when (R::class) {
//            Result::class -> (fn(it) as Result<String>    ).fold({Pair(it,0)}, {Pair(""" "$it" """,1)})
//            String::class -> runCatching {fn(it) as String}.fold({Pair(it,0)}, {Pair(""" "$it" """,1)} )
//            Unit::class   -> fn(it).let { null }
//            Nothing::class-> runCatching{ fn(it) }.fold({error("Unexpected Behavior: fun (*)->Nothing runs successfully.")}, {Pair(""" "$it" """,1)})
//            Any::class-> runCatching {fn(it) as String}.fold({Pair(it,0)}, {Pair(""" "$it" """,1)} )
//            else -> throw IllegalArgumentException(R::class.simpleName)
//        }
//    }
//}

// vararg not supported in lambda
// implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
// import kotlinx.serialization.Serializable
// import kotlinx.serialization.decodeFromString
// import kotlinx.serialization.encodeToString
// import kotlinx.serialization.json.Json
// @Deprecated("Experimental")
// inline fun <reified T :@Serializable Any, reified R :@Serializable Any> bindEx(name :String, crossinline fn :(vararg @Serializable T)-> @Serializable R) = bind(name) {
//         Json.encodeToString(fn(Json.decodeFromString(it)))
//     }