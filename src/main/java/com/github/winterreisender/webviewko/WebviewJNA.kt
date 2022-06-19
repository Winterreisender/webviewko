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

import com.sun.jna.*
import java.nio.file.CopyOption
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

// JNA Bindings
// TODO: Document these bindings using webview's document
// In the most ideal situation, Pointer.NULL should not be null for type safe.
// But we have Pointer.NULL == null and thus Pointer.NULL is not Pointer. This is an Kotlin/Java inteop issue
// So we always use `Pointer?` for C interop.




class WebviewJNA {
    companion object {
        const val JNA_LIBRARY_NAME = "webview"

        // Window size hints, in API layer, we have enum
        const val WEBVIEW_HINT_NONE = 0 // Width and height are default size
        const val WEBVIEW_HINT_MIN = 1  // Width and height are minimum bounds
        const val WEBVIEW_HINT_MAX = 2  // Width and height are maximum bounds
        const val WEBVIEW_HINT_FIXED = 3 // Window size can not be changed by a user

        fun getLibOrNull() :WebviewLibrary? {
            // move WebView2Loader.dll to System.setProperty("jna.tmpdir",".") in Windows
            if (Platform.getOSType() == Platform.WINDOWS) {
                val file = Native.extractFromResourcePath("WebView2Loader.dll",WebviewJNA::class.java.classLoader)
                val dest = file.toPath().parent.resolve("WebView2Loader.dll")
                // Why there's not something like DO_NOTHING_IF_EXISTING?
                if(Files.notExists(dest)) {
                    Files.move(file.toPath(),dest)
                }
            }
            return Native.load("webview", WebviewLibrary::class.java)
        }

        fun getLib() :WebviewLibrary = getLibOrNull()!!

        @Deprecated("",ReplaceWith("getLib"))
        fun getInstance() :WebviewLibrary = getLib()

    }

    interface WebviewLibrary : Library {
        fun webview_create(debug :Int = 0, window :Pointer? = Pointer.NULL) :Pointer?

        // Destroys a webview and closes the native window.
        fun webview_destroy(webview :Pointer?)
        fun webview_run(webview :Pointer?)

        // Stops the main loop. It is safe to call this function from another other
        // background thread.
        fun webview_terminate(webview :Pointer?)

        // Posts a function to be executed on the main thread. You normally do not need
        // to call this function, unless you want to tweak the native window.
        @Deprecated("You normally do not need it, unless you want to tweak the native window")
        fun webview_dispatch(webview :Pointer?, fn: webview_dispatch_fn_callback, args :Pointer?)

        interface webview_dispatch_fn_callback : Callback {
            fun apply(webview :Pointer?, arg :Pointer? = Pointer.NULL)
        }

        /*
         * Not mapped by webview_csharp
         *
         * Returns a native window handle pointer. When using GTK backend the pointer
         * is GtkWindow pointer, when using Cocoa backend the pointer is NSWindow
         * pointer, when using Win32 backend the pointer is HWND pointer.
         */
        @Deprecated("Not suggested to use")
        fun webview_get_window(webview :Pointer?) :Pointer?

        fun webview_set_title(webview :Pointer?, title :String)
        fun webview_set_size(webview :Pointer?, width :Int, height :Int, hints :Int)

        fun webview_navigate(webview :Pointer?, url :String)
        fun webview_set_html(webview :Pointer?, html :String)

        //Injects JavaScript code at the initialization of the new page. Every time
        //the webview will open a new page - this initialization code will be
        //executed. It is guaranteed that code is executed before window.onload.
        fun webview_init(webview :Pointer?, js :String)

        //Evaluates arbitrary JavaScript code. Evaluation happens asynchronously, also
        //the result of the expression is ignored. Use the bind function if you want to
        //receive notifications about the results of the evaluation.
        fun webview_eval(webview :Pointer?, js :String)
        fun webview_bind(webview :Pointer?, name :String, callback: webview_bind_fn_callback, arg :Pointer? = Pointer.NULL)
        interface webview_bind_fn_callback : Callback {
            fun apply(seq :String?, req :String?, arg :Pointer? = Pointer.NULL)
        }

        fun webview_return(webview :Pointer?, seq :String?, status :Int, result :String)

    }
}

