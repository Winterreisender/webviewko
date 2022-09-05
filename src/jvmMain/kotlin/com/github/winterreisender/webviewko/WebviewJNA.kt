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

import com.sun.jna.*
import java.nio.file.Files

/**
 * A JNA binding to webview for Kotlin/JVM and Java
 *
 * This class contains an interface that mapping Kotlin/JVM and webview, functions to get the webview lib, and some constants needed by webview
 *
 */
class WebviewJNA {
    companion object {
        /**
         * The name of0 JNA LIBRARY
         */
        const val JNA_LIBRARY_NAME = "webview"

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


        /**
         * Return a WebviewLibrary or null (if failed) contains native webview functions.
         *
         * @return a `WebviewLibrary` contains native webview functions or null if failed to load
         *
         */
        fun getLibOrNull() : WebviewLibrary? {
            if (Platform.getOSType() == Platform.WINDOWS) {
                val file = Native.extractFromResourcePath("WebView2Loader.dll", WebviewJNA::class.java.classLoader)
                val dest = file.toPath().parent.resolve("WebView2Loader.dll")
                if(Files.notExists(dest)) {
                    Files.move(file.toPath(), dest)
                }
            }
            return Native.load("webview", WebviewLibrary::class.java)
        }

        /**
         * Return a WebviewLibrary contains native webview functions.
         *
         * @return a `WebviewLibrary` contains native webview functions
         * @throws `Exception` if failed to load webview lib
         *
         */
        fun getLib() : WebviewLibrary = getLibOrNull()  ?: throw Exception("Failed to load webview")
    }

    /**
     * All 15 C functions in webview.h
     */

    interface WebviewLibrary : Library {
        /**
         * Creates a new webview instance.
         *
         * If debug is non-zero - developer tools will be enabled (if the platform supports them). Window parameter can be a pointer to the native window handle. If it's non-null - then child WebView is embedded into the given parent window. Otherwise, a new window is created. Depending on the platform, a GtkWindow, NSWindow or HWND pointer can be passed here.
         *
         * @return a webview handle
         */
        fun webview_create(debug :Int = 0, window : Pointer? = Pointer.NULL) : Pointer?

        // Destroys a webview and closes the native window.
        fun webview_destroy(webview : Pointer?)

        /**
         * Runs the main loop until it's terminated.
         *
         * After this function exits - you must destroy the webview.
         *
         * @param webview the handle of webview, usually returned by `webview_create`
         */
        fun webview_run(webview : Pointer?)


        /**
         * Stops the main loop.
         *
         * It is safe to call this function from another other background thread.
         *
         * @param webview the handle of webview, usually returned by `webview_create`
         */
        fun webview_terminate(webview : Pointer?)

        /**
         * Posts a function to be executed on the main thread.
         *
         * It safely schedules the callback to be run on the main thread on the next main loop iteration.
         * You normally do not need to call this function, unless you want to tweak the native window.
         *
         * @param webview the handle of webview, usually returned by `webview_create`
         * @param fn the callback
         * @param args please ignore it and keep it `Pointer.NULL` unless you know what you're doing.
         */
        fun webview_dispatch(webview : Pointer?, fn: webview_dispatch_fn_callback, args : Pointer? = Pointer.NULL)
        interface webview_dispatch_fn_callback : Callback {
            fun apply(webview : Pointer?, arg : Pointer? = Pointer.NULL)
        }

        /**
         * Returns a native window handle pointer.
         *
         * When using GTK backend the pointer is GtkWindow pointer, when using Cocoa backend the pointer is NSWindow pointer, when using Win32 backend the pointer is HWND pointer.
         *
         * @param webview the handle of webview, usually returned by `webview_create`
         */
        @Deprecated("Not suggested to use")
        fun webview_get_window(webview : Pointer?) : Pointer?

        /**
         * Updates the title of the native window.
         *
         * Must be called from the UI thread.
         *
         * @param webview the handle of webview, usually returned by `webview_create`
         * @param title the new title
         */
        fun webview_set_title(webview : Pointer?, title :String)

        /**
         * Updates the size of the native window.
         *
         * Accepts a WEBVIEW_HINT
         *
         * @param webview the handle of webview, usually returned by `webview_create`
         * @param hints can be one of `WEBVIEW_HINT_NONE`, `WEBVIEW_HINT_MIN`, `WEBVIEW_HINT_MAX` or `WEBVIEW_HINT_FIXED`
         */
        fun webview_set_size(webview : Pointer?, width :Int, height :Int, hints :Int = WEBVIEW_HINT_NONE)

        /**
         * Navigates webview to the given URL
         *
         * URL may be a data URI, i.e. "data:text/text,...". It is often ok not to url-encode it properly, webview will re-encode it for you.
         *
         * @param webview the handle of webview, usually returned by `webview_create`
         * @param url the URL or URI
         * */
        fun webview_navigate(webview : Pointer?, url :String)


        /**
         * Set webview HTML directly.
         *
         * @param webview the handle of webview, usually returned by `webview_create`
         * @param html the HTML content
         */
        fun webview_set_html(webview : Pointer?, html :String)

        /**
         * Injects JavaScript code at the initialization of the new page.
         *
         * Every time the webview will open a new page - this initialization code will be executed. It is guaranteed that code is executed before `window.onload`.
         *
         * @param webview the handle of webview, usually returned by `webview_create`
         * @param js the JS code
         */
        fun webview_init(webview : Pointer?, js :String)

        /**
         * Evaluates arbitrary JavaScript code.
         *
         * Evaluation happens asynchronously, also the result of the expression is ignored. Use the `webview_bind` function if you want to receive notifications about the results of the evaluation.
         *
         * @param webview the handle of webview, usually returned by `webview_create`
         * @param js the JS code
         */
        fun webview_eval(webview : Pointer?, js :String)

        /**
         * Binds a Kotlin or Java callback so that it will appear under the given name as a global JavaScript function.
         *
         * Callback receives a request string. Request string is a JSON array of all the arguments passed to the JavaScript function. Internally it uses `webview_init`.
         *
         * @param webview the handle of webview, usually returned by `webview_create`
         * @param name the name of the global JavaScript function
         * @param callback the Kotlin/Java callback function wrapper in an interface, use `webview_return` to response to the JS request.
         * @param arg the context. please keep it `Pointer.NULL` unless you know what you're doing.
         */
        fun webview_bind(webview : Pointer?, name :String, callback: webview_bind_fn_callback, arg : Pointer? = Pointer.NULL)

        /**
         * Removes a Kotlin/Java callback that was previously set by `webview_bind`.
         *
         * @param webview the handle of webview, usually returned by `webview_create`
         * @param name the name of JS function used in `webview_bind`
         */
        fun webview_unbind(webview : Pointer?, name :String)

        /**
         * The callback interface for `webview_bind` due to JNA's function mapping rules.
         */
        interface webview_bind_fn_callback : Callback {
            /**
             * The callback function
             *
             * You should use `webview_return` to response to the JS
             *
             * @param seq the id of request
             * @param req request string is a JSON array of all the arguments passed to the JavaScript function.
             * @param arg please ignore it and keep it `Pointer.NULL` unless you know what you're doing.
             */
            fun apply(seq :String?, req :String?, arg : Pointer? = Pointer.NULL)
        }

        /**
         * Allows to return a value from the Kotlin/Java binding.
         *
         * Original request pointer must be provided to help internal RPC engine match requests with responses.  It is similar to `webview::resolve` in C++ version.
         *
         * @param webview the handle of webview, usually returned by `webview_create`
         * @param seq the id of request to response
         * @param status If status is zero - result is expected to be a valid JSON result value. If status is not zero - result is an error JSON object.
         * @param result the JSON result value to response
         */
        fun webview_return(webview : Pointer?, seq :String?, status :Int, result :String)

    }
}