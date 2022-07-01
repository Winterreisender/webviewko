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
import webview.*


/**
 * A Native binding to webview for Kotlin/Native
 */
object WebviewNative {
    /**
     * Width and height are default size
     */
    const val WEBVIEW_HINT_NONE = 0

    /**
     * Width and height are minimum bounds
     */
    const val WEBVIEW_HINT_MIN = 1

    /**
     * Width and height are maximum bounds
     */
    const val WEBVIEW_HINT_MAX = 2

    /**
     * Window size can not be changed by a user
     */
    const val WEBVIEW_HINT_FIXED = 3

    //All 15 C functions in webview.h

    /**
     * Creates a new webview instance.
     *
     * If debug is non-zero - developer tools will be enabled (if the platform supports them). Window parameter can be a pointer to the native window handle. If it's non-null - then child WebView is embedded into the given parent window. Otherwise, a new window is created. Depending on the platform, a GtkWindow, NSWindow or HWND pointer can be passed here.
     *
     * @return a webview handle
     */
    fun webview_create(debug :Int = 0, window :COpaquePointer? = null) :webview_t? = webview.webview_create(debug, window)

    // Destroys a webview and closes the native window.
    fun webview_destroy(w :webview_t?) = webview.webview_destroy(w)

    /**
     * Runs the main loop until it's terminated.
     *
     * After this function exits - you must destroy the webview.
     *
     * @param w the handle of webview, usually returned by `webview_create`
     */
    fun webview_run(w :webview_t?) = webview.webview_run(w)


    /**
     * Stops the main loop.
     *
     * It is safe to call this function from another other background thread.
     *
     * @param w the handle of webview, usually returned by `webview_create`
     */
    fun webview_terminate(w :webview_t?) = webview.webview_terminate(w)

    /**
     * Posts a function to be executed on the main thread.
     *
     * It safely schedules the callback to be run on the main thread on the next main loop iteration.
     * You normally do not need to call this function, unless you want to tweak the native window.
     *
     * @param w the handle of webview, usually returned by `webview_create`
     * @param fn the callback
     * @param args the arguments for `fn`
     */
    fun webview_dispatch(w :webview_t?, fn :CPointer<CFunction<(webview_t?, COpaquePointer?) -> Unit>>?, args :CValuesRef<*>) = webview.webview_dispatch(w,fn,args)

    /**
     * Returns a native window handle pointer.
     *
     * When using GTK backend the pointer is GtkWindow pointer, when using Cocoa backend the pointer is NSWindow pointer, when using Win32 backend the pointer is HWND pointer.
     *
     * @param w the handle of webview, usually returned by `webview_create`
     */
    fun webview_get_window(w :webview_t?) = webview.webview_get_window(w)

    /**
     * Updates the title of the native window.
     *
     * Must be called from the UI thread.
     *
     * @param w the handle of webview, usually returned by `webview_create`
     * @param title the new title
     */
    fun webview_set_title(w :webview_t?, title :String) = webview.webview_set_title(w,title)

    /**
     * Updates the size of the native window.
     *
     * Accepts a WEBVIEW_HINT
     *
     * @param w the handle of webview, usually returned by `webview_create`
     * @param hints can be one of `WEBVIEW_HINT_NONE`, `WEBVIEW_HINT_MIN`, `WEBVIEW_HINT_MAX` or `WEBVIEW_HINT_FIXED`
     */
    fun webview_set_size(w :webview_t?, width :Int, height :Int, hints :Int) = webview.webview_set_size(w,width,height,hints)

    /**
     * Navigates webview to the given URL
     *
     * URL may be a data URI, i.e. "data:text/text,...". It is often ok not to url-encode it properly, webview will re-encode it for you.
     *
     * @param w the handle of webview, usually returned by `webview_create`
     * @param url the URL or URI
     * */
    fun webview_navigate(w :webview_t?, url : String) = webview.webview_navigate(w,url)


    /**
     * Set webview HTML directly.
     *
     * @param w the handle of webview, usually returned by `webview_create`
     * @param html the HTML content
     */
    fun webview_set_html(w :webview_t?, html :String) = webview.webview_navigate(w, html)

    /**
     * Injects JavaScript code at the initialization of the new page.
     *
     * Every time the webview will open a new page - this initialization code will be executed. It is guaranteed that code is executed before `window.onload`.
     *
     * @param w the handle of webview, usually returned by `webview_create`
     * @param js the JS code
     */
    fun webview_init(w :webview_t?, js :String) = webview.webview_init(w,js)

    /**
     * Evaluates arbitrary JavaScript code.
     *
     * Evaluation happens asynchronously, also the result of the expression is ignored. Use the `webview_bind` function if you want to receive notifications about the results of the evaluation.
     *
     * @param w the handle of webview, usually returned by `webview_create`
     * @param js the JS code
     */
    fun webview_eval(w :webview_t?, js :String) = webview.webview_eval(w, js)

    /**
     * Binds a native Kotlin/Java callback so that it will appear under the given name as a global JavaScript function.
     *
     * Callback receives a request string. Request string is a JSON array of all the arguments passed to the JavaScript function. Internally it uses `webview_init`.
     *
     * @param w the handle of webview, usually returned by `webview_create`
     * @param name the name of the global JavaScript function
     * @param callback the Kotlin/Java callback function wrapper in an interface, use `webview_return` to response to the JS request.
     * @param arg the context. please keep it `Pointer.NULL` unless you know what you're doing.
     */
    fun webview_bind(w :webview_t?, name :String, callback:  CPointer<CFunction<(CPointer<ByteVar /* = ByteVarOf<Byte> */>?, CPointer<ByteVar>?, COpaquePointer?) -> Unit>>?, arg :CValuesRef<*>) =
        webview.webview_bind(w,name,callback,arg)

    /**
     * Removes a Kotlin/Java callback that was previously set by `webview_bind`.
     *
     * @param w the handle of webview, usually returned by `webview_create`
     * @param name the name of JS function used in `webview_bind`
     */
    fun webview_unbind(w :webview_t?, name :String) = webview.webview_unbind(w, name)


    /**
     * Allows to return a value from the Kotlin/Java binding.
     *
     * Original request pointer must be provided to help internal RPC engine match requests with responses.  It is similar to `webview::resolve` in C++ version.
     *
     * @param w the handle of webview, usually returned by `webview_create`
     * @param seq the id of request to response
     * @param status If status is zero - result is expected to be a valid JSON result value. If status is not zero - result is an error JSON object.
     * @param result the JSON result value to response
     */
    fun webview_return(w :webview_t?, seq :String?, status :Int, result :String) = webview.webview_return(w,seq,status,result)

}