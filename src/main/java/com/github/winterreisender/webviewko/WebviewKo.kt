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


enum class WindowHint(val value :Int) {
    None(WebviewJNA.WEBVIEW_HINT_NONE), // Width and height are default size
    Minimum(WebviewJNA.WEBVIEW_HINT_MIN),  // Width and height are minimum bounds
    Maximum(WebviewJNA.WEBVIEW_HINT_MAX),  // Width and height are m bounds
    Fixed(WebviewJNA.WEBVIEW_HINT_FIXED) // Window size can not be changed by a user
}

class WebviewKo(debug: Int = 0) {
    private val lib: WebviewLibrary = WebviewJNA.getLib()
    private val pWebview: Pointer = lib.webview_create(debug,Pointer.NULL)!!
    // If you add a window parameter, you need to add it to constructor, thus your user must have access to com.sun.jna.Pointer
    // then your user must install jna whether they want. Or they'll get an error: Cannot access class 'com.sun.jna.Pointer'
    fun title(v: String) = lib.webview_set_title(pWebview, v)
    fun url(v: String) = lib.webview_navigate(pWebview, v)
    fun html(v :String) = lib.webview_set_html(pWebview, v)
    fun size(width: Int, height: Int, hint: WindowHint = WindowHint.None) = lib.webview_set_size(pWebview, width, height, hint.value)
    fun initJS(js :String) = lib.webview_init(pWebview, js)
    fun eval(js :String) = lib.webview_eval(pWebview, js)

    // This does not work
    //fun <T,R> bind(name :String, fn :(T)-> R, x :Int) = lib.webview_bind(pWebview, name, object :WebviewLibrary.webview_bind_fn_callback {
    //    override fun apply(seq: String?, req: String?, arg: Pointer?) {
    //        val msg = Json.decodeFromString<T>(req!!)
    //        lib.webview_return(pWebview, seq, 0, Json.encodeToString(fn(req)))
    //    }
    //})
    fun bind(name :String, fn :WebviewKo.(String?)->String) = lib.webview_bind(pWebview, name, object :WebviewLibrary.webview_bind_fn_callback {
        override fun apply(seq: String?, req: String?, arg: Pointer?) {
            lib.webview_return(pWebview, seq, 0, fn(req))
        }
    })
    fun show() {
        lib.webview_run(pWebview)
        lib.webview_destroy(pWebview)
    }

    fun terminate() {
        lib.webview_terminate(pWebview)
    }
}