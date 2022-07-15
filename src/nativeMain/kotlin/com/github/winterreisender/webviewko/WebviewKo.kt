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

import kotlinx.cinterop.*
import com.github.winterreisender.cwebview.*
import kotlin.native.concurrent.freeze

typealias BindContext = Pair<WebviewKo,WebviewKo.(String?) -> String>
typealias DispatchContext = Pair<WebviewKo,WebviewKo.() ->Unit>

/**
 * The Kotlin/Native binding to webview in Kotlin
 */

actual class WebviewKo actual constructor(debug: Int) {

    // Freeze the object (disable changes) for sharing between threads
    private val w :webview_t

    init {
        w = webview_create(debug, null)!!
        freeze()
    }

    /**
     * Updates the title of the native window.
     *
     * Must be called from the UI thread.
     *
     * @param v the new title
     */
    actual fun title(v: String) = webview_set_title(w,v)


    /**
     * Navigates webview to the given URL
     *
     * URL may be a data URI, i.e. "data:text/text,...". It is often ok not to url-encode it properly, webview will re-encode it for you. Same as [navigate]
     *
     * @param v the URL or URI
     * */
    actual fun url(v: String) = navigate(v)

    /**
     * Navigates webview to the given URL
     *
     * URL may be a data URI, i.e. "data:text/text,...". It is often ok not to url-encode it properly, webview will re-encode it for you. Same as [url]
     *
     * @param v the URL or URI
     * */
    actual fun navigate(v: String) = webview_navigate(w,v)

    /**
     * Set webview HTML directly.
     *
     * @param v the HTML content
     */
    actual fun html(v: String) = webview_set_html(w,v)


    actual enum class WindowHint(v :Int) {
        None(WEBVIEW_HINT_NONE),
        Min(WEBVIEW_HINT_MIN),
        Max(WEBVIEW_HINT_MAX),
        Fixed(WEBVIEW_HINT_FIXED)
    }
    /**
     * Updates the size of the native window.
     *
     * Accepts a WEBVIEW_HINT
     *
     * @param hints can be one of `WEBVIEW_HINT_NONE`, `WEBVIEW_HINT_MIN`, `WEBVIEW_HINT_MAX` or `WEBVIEW_HINT_FIXED`
     */
    actual fun size(width: Int, height: Int, hints: WindowHint) =
        webview_set_size(w, width, height, hints.ordinal)

    /**
     * Injects JS code at the initialization of the new page.
     *
     * Same as `initJS`. Every time the webview will open a new page - this initialization code will be executed. It is guaranteed that code is executed before window.onload.
     *
     * @param js the JS code
     */
    actual fun init(js: String) = webview_init(w,js)

    /**
     * Evaluates arbitrary JS code.
     *
     * Evaluation happens asynchronously, also the result of the expression is ignored. Use the `webview_bind` function if you want to receive notifications about the results of the evaluation.
     *
     * @param js the JS code
     */
    actual fun eval(js: String) = webview_eval(w,js)


    /**
     * Binds a Kotlin function callback so that it will appear under the given name as a global JS function.
     *
     * Callback receives a request string. Request string is a JSON array of all the arguments passed to the JS function. If you need binding a C function, see [WebviewKo.cBind]
     *
     * @param name the name of the global JS function
     * @param fn the callback function which receives the request parameter in JSON as input and return the response to JS in JSON. In Java the fn should be String response(WebviewKo webview, String request)
     */
    actual fun bind(name: String, fn: WebviewKo.(String?) -> String) {
        val ctx = StableRef.create(BindContext(this, fn)).freeze() // typealias BindCtx == Pair
        webview_bind(
            w,name,
            staticCFunction { seq,req,arg ->
                initRuntimeIfNeeded()
                val (webviewKo,callback) = arg!!.asStableRef<BindContext>().get()
                webview_return(
                    webviewKo.w, seq?.toKString(),0,
                    callback(webviewKo, req?.toKString())
                )
            },
            ctx.asCPointer()
        )
        // TODO: Prevent memory leak using ctx.dispose()
        // The correct time to use ctx.dispose() is after the final callback, before the webview_destroy. So,there are 4 solution:
        // 1. Make a thick wrapper like Go bindings, add glue code, binding array etc.
        // 2. Wait for webview_unbind(w, arg), add binding name array and cleanup all contexts in WebviewKo.terminate and WebviewKo.run
        // 3. Do nothing. About 16 bytes memory leak per bind is not unacceptable in "Modern Software Engineering" (?
        // 4. Leave it to the users. Provide pure C API. See [cBind]
    }

    /**
     * Removes a callback that was previously set by `webview_bind`.
     *
     * @param name the name of JS function used in `webview_bind`
     */
    actual fun unbind(name: String) = webview_unbind(w,name)

    /**
     * Posts a function to be executed on the main thread.
     *
     * It safely schedules the callback to be run on the main thread on the next main loop iteration. Like `invokeLater` in Swing
     *
     * @param fn the function to be executed on the main thread.
     *
     */
    actual fun dispatch(fn: WebviewKo.() -> Unit) {
        val ctx = StableRef.create(DispatchContext(this,fn)).freeze()
        webview_dispatch(
            w,
            staticCFunction { w,arg ->
                initRuntimeIfNeeded()
                val (webviewKo,callback) = arg!!.asStableRef<DispatchContext>().get()
                callback(webviewKo)
            },
            ctx.asCPointer()
        )
    }

    /**
     * Runs the main loop and destroy it when terminated.
     *
     * This will block the thread.
     */
    actual fun show() {
        webview_run(w)
        webview_destroy(w)
    }

    /**
     * Stops the main loop.
     *
     * It is safe to call this function from another other background thread.
     *
     */
    actual fun terminate() = webview_terminate(w)

    /**
     * Return the C Pointer of the webview.
     *
     * @return the C Pointer (webview_t) of the webview
     *
     */
    fun getWebviewPointer() = w

    /**
     * Binds a C callback so that it will appear under the given name as a global JS function.
     *
     * Callback receives a request string. Request string is a JSON array of all the arguments passed to the JS function. Internally it uses `webview_init`. If you need binding a Kotlin function, see [WebviewKo.bind]
     *
     * @param name the name of the global JS function
     * @param callback the C callback function [staticCFunction].
     * @param arg the context.
     */
    fun cBind(name :String, callback:  CPointer<CFunction<(CPointer<ByteVar /* = ByteVarOf<Byte> */>?, CPointer<ByteVar>?, COpaquePointer?) -> Unit>>?, arg :CValuesRef<*>) =
        webview_bind(w,name,callback,arg)

    /**
     * Posts a C function to be executed on the main thread.
     *
     * It safely schedules the callback to be run on the main thread on the next main loop iteration.
     * You normally do not need to call this function, unless you want to tweak the native window.
     *
     * @param fn the callback [staticCFunction]
     * @param args the arguments for `fn`
     */
    fun cDispatch(fn :CPointer<CFunction<(webview_t?, COpaquePointer?) -> Unit>>?, args :CValuesRef<*>)
        = webview_dispatch(w,fn,args)
}

