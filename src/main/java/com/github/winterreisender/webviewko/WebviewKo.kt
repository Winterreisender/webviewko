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

package com.github.winterreisender.webviewko

import com.sun.jna.Pointer
import com.github.winterreisender.webviewko.WebviewJNA.WebviewLibrary

/**
 * The window size hints used by `WebviewKo.size`
 *
 * A Wrapper of WEBVIEW_HINT_NONE, WEBVIEW_HINT_MIN, WEBVIEW_HINT_MAX and WEBVIEW_HINT_FIXED
 *
 */
enum class WindowHint(val value :Int) {
    /**
    * Width and height are default size
     */
    None(WebviewJNA.WEBVIEW_HINT_NONE),

    /**
     * Width and height are minimum bounds
     */
    Minimum(WebviewJNA.WEBVIEW_HINT_MIN),

    /**
     * Width and height are maximum bounds
     */
    Maximum(WebviewJNA.WEBVIEW_HINT_MAX),

    /**
     * Window size can not be changed by a user
     */
    Fixed(WebviewJNA.WEBVIEW_HINT_FIXED)
}


/**
 * The High level binding to webview in Kotlin
 */
class WebviewKo(debug: Int = 0) {
    private val lib: WebviewLibrary = WebviewJNA.getLib()
    private val pWebview: Pointer = lib.webview_create(debug,Pointer.NULL)!!


    // If you add a window parameter, you need to add it to constructor, thus your user must have access to com.sun.jna.Pointer
    // then your user must install jna whether they want. Or they'll get an error: Cannot access class 'com.sun.jna.Pointer'
    // Even if you use the api() in Gradle, adding it to constructor made it impossible to exclude JNA for users

    /**
     * Updates the title of the native window.
     *
     * Must be called from the UI thread.
     *
     * @param v the new title
     */
    fun title(v: String) = lib.webview_set_title(pWebview, v)

    /**
     * Navigates webview to the given URL
     *
     * URL may be a data URI, i.e. "data:text/text,...". It is often ok not to url-encode it properly, webview will re-encode it for you. Same as [navigate]
     *
     * @param v the URL or URI
     * */
    fun url(v: String) = lib.webview_navigate(pWebview, v)

    /**
     * Navigates webview to the given URL
     *
     * URL may be a data URI, i.e. "data:text/text,...". It is often ok not to url-encode it properly, webview will re-encode it for you. Same as [url]
     *
     * @param v the URL or URI
     * */
    fun navigate(v: String) = url(v)

    /**
     * Set webview HTML directly.
     *
     * @param v the HTML content
     */
    fun html(v :String) = lib.webview_set_html(pWebview, v)

    /**
     * Updates the size of the native window.
     *
     * Accepts a WEBVIEW_HINT
     *
     * @param hints can be one of `WEBVIEW_HINT_NONE`, `WEBVIEW_HINT_MIN`, `WEBVIEW_HINT_MAX` or `WEBVIEW_HINT_FIXED`
     */
    fun size(width: Int, height: Int, hints: WindowHint = WindowHint.None) = lib.webview_set_size(pWebview, width, height, hints.value)

    /**
     * Injects JavaScript code at the initialization of the new page.
     *
     * Every time the webview will open a new page - this initialization code will be executed. It is guaranteed that code is executed before window.onload.
     *
     * @param js the JS code
     */
    fun initJS(js :String) = lib.webview_init(pWebview, js)

    /**
     * Injects JavaScript code at the initialization of the new page.
     *
     * Same as `initJS`. Every time the webview will open a new page - this initialization code will be executed. It is guaranteed that code is executed before window.onload.
     *
     * @param js the JS code
     */
    fun init(js :String) = initJS(js)

    /**
     * Evaluates arbitrary JavaScript code.
     *
     * Evaluation happens asynchronously, also the result of the expression is ignored. Use the `webview_bind` function if you want to receive notifications about the results of the evaluation.
     *
     * @param js the JS code
     */
    fun eval(js :String) = lib.webview_eval(pWebview, js)

    // This does not work
    //fun <T,R> bind(name :String, fn :(T)-> R, x :Int) = lib.webview_bind(pWebview, name, object :WebviewLibrary.webview_bind_fn_callback {
    //    override fun apply(seq: String?, req: String?, arg: Pointer?) {
    //        val msg = Json.decodeFromString<T>(req!!)
    //        lib.webview_return(pWebview, seq, 0, Json.encodeToString(fn(req)))
    //    }
    //})

    /**
     * Binds a native Kotlin/Java callback so that it will appear under the given name as a global JavaScript function.
     *
     * Callback receives a request string. Request string is a JSON array of all the arguments passed to the JavaScript function.
     *
     * @param name the name of the global JavaScript function
     * @param fn the callback function which receives the request parameter in JSON as input and return the response to JS in JSON. In Java the fn should be String response(WebviewKo webview, String request)
     */
    fun bind(name :String, fn :WebviewKo.(String?)->String) = lib.webview_bind(pWebview, name, object :WebviewLibrary.webview_bind_fn_callback {
        override fun apply(seq: String?, req: String?, arg: Pointer?) {
            lib.webview_return(pWebview, seq, 0, fn(req))
        }
    })

    /**
     * Removes a callback that was previously set by `webview_bind`.
     *
     * @param name the name of JS function used in `webview_bind`
     */
    fun unbind(name: String) = lib.webview_unbind(pWebview, name)


    /**
     * Posts a function to be executed on the main thread.
     *
     * It safely schedules the callback to be run on the main thread on the next main loop iteration. Like `invokeLater` in Swing
     *
     * @param fn the function to be executed on the main thread.
     *
     */
    fun dispatch(fn :WebviewKo.()->Unit) = lib.webview_dispatch(pWebview,object :WebviewLibrary.webview_dispatch_fn_callback {
        override fun apply(webview: Pointer?, arg: Pointer?) {
            fn()
        }
    })


    /**
     * Runs the main loop and destroy it when terminated.
     *
     * This will block the thread.
     */
    fun show() {
        lib.webview_run(pWebview)
        lib.webview_destroy(pWebview)
    }

    /**
     * Stops the main loop.
     *
     * It is safe to call this function from another other background thread.
     *
     */
    fun terminate() = lib.webview_terminate(pWebview)
}