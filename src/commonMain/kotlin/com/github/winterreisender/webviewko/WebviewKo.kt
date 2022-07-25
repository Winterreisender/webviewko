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

/**
 * The Kotlin Multiplatform binding to webview
 */
expect class WebviewKo(debug: Int = 0) {

    /**
     * Updates the title of the native window.
     *
     * Must be called from the UI thread.
     *
     * @param v the new title
     */
    fun title(v: String)

    /**
     * Navigates webview to the given URL
     *
     * URL may be a data URI, i.e. "data:text/text,...". It is often ok not to url-encode it properly, webview will re-encode it for you. Same as [navigate]
     *
     * @param v the URL or URI
     * */
    fun url(v: String)

    /**
     * Navigates webview to the given URL
     *
     * URL may be a data URI, i.e. "data:text/text,...". It is often ok not to url-encode it properly, webview will re-encode it for you. Same as [url]
     *
     * @param v the URL or URI
     * */
    fun navigate(v: String)

    /**
     * Set webview HTML directly.
     *
     * @param v the HTML content
     */
    fun html(v :String)

    /**
     * Updates the size of the native window.
     *
     * Accepts a WEBVIEW_HINT
     *
     * @param hints can be one of `WEBVIEW_HINT_NONE`, `WEBVIEW_HINT_MIN`, `WEBVIEW_HINT_MAX` or `WEBVIEW_HINT_FIXED`
     */
    fun size(width: Int, height: Int, hints: WindowHint = WindowHint.None)

    /**
     * The window size hints used by `WebviewKo.size`
     *
     * A Wrapper of WEBVIEW_HINT_NONE, WEBVIEW_HINT_MIN, WEBVIEW_HINT_MAX and WEBVIEW_HINT_FIXED
     *
     */
    enum class WindowHint {
        None,
        Min,
        Max,
        Fixed
    }

    /**
     * Injects JS code at the initialization of the new page.
     *
     * Same as `initJS`. Every time the webview will open a new page - this initialization code will be executed. It is guaranteed that code is executed before window.onload.
     *
     * @param js the JS code
     */
    fun init(js :String)

    /**
     * Evaluates arbitrary JS code.
     *
     * Evaluation happens asynchronously, also the result of the expression is ignored. Use the `webview_bind` function if you want to receive notifications about the results of the evaluation.
     *
     * @param js the JS code
     */
    fun eval(js :String)


    /**
     * Binds a Kotlin callback so that it will appear under the given name as a global JS function.
     *
     * Callback `fn` receives a request String, which is a JSON array of all the arguments passed to the JS function and returns `Pair<String,Int>(Response,Status)?`.
     * If status is zero - result is expected to be a valid JSON result value. If status is not zero - result is an error JSON object.

     * @param name the name of the global JS function
     * @param fn the callback function which receives the request parameter in JSON as input and return the response JSON and status. If it returns null, the webview won't receive a feedback.
     */
    fun bindX(name :String, fn :WebviewKo.(String?)->Pair<String,Int>?)

    /**
     * Binds a Kotlin callback so that it will appear under the given name as a global JS function.
     *
     * Callback `fn` receives a request String, which is a JSON array of all the arguments passed to the JS function and returns `Unit`,`String` or `Pair<String,Int>`.
     *
     * @param name the name of the global JS function
     * @param fn the callback function which receives the request parameter in JSON as input and return the response JSON and status.When `fn` return `Pair(Response,Status)` the webview will receive the response and status . When `fn` returns `String`, the Status is 0. When `fn` returns `Unit`, the webview won't receive a feedback.
     */
    inline fun <reified R : Any> bind(name :String, crossinline fn: WebviewKo.(String?) -> R)


    /**
     * Removes a callback that was previously set by `webview_bind`.
     *
     * @param name the name of JS function used in `webview_bind`
     */
    fun unbind(name: String)


    /**
     * Posts a function to be executed on the main thread.
     *
     * It safely schedules the callback to be run on the main thread on the next main loop iteration.
     *
     * @param fn the function to be executed on the main thread.
     *
     */
    fun dispatch(fn : WebviewKo.()->Unit)


    /**
     * Runs the main loop and destroy it when terminated.
     *
     * This will block the thread.
     */
    fun show()

    /**
     * Stops the main loop.
     *
     * It is safe to call this function from another other background thread.
     *
     */
    fun terminate()

}