package com.github.winterreisender.webviewko

import com.sun.jna.Pointer
import java.net.URI

enum class WindowHint(val value :Int) {
    None(WebviewJNA.WEBVIEW_HINT_NONE), // Width and height are default size
    Minimum(WebviewJNA.WEBVIEW_HINT_MIN),  // Width and height are minimum bounds
    Maximum(WebviewJNA.WEBVIEW_HINT_MAX),  // Width and height are m bounds
    Fixed(WebviewJNA.WEBVIEW_HINT_FIXED) // Window size can not be changed by a user
}

class WebviewKo(
    var title: String = "",
    var urlStr: String = "about:blank",
    var width :Int = 600,
    var height :Int = 800,
    var windowHint: WindowHint = WindowHint.None,
    var initJS: String? = null, // TODO: implement onLoad and jsCallback
    var jsCallback: String? = null
) {
    fun show() {
        with(WebviewJNA.getInstance()) {
            val pWebview = webview_create(0, Pointer.NULL)
            // webview_bind(pWebview,"",onLoad,null)

            initJS?.let { webview_init(pWebview, it) };

            webview_set_title(pWebview, title)
            webview_set_size(pWebview, width, height, windowHint.value)
            webview_navigate(pWebview, urlStr)
            webview_run(pWebview)
            webview_destroy(pWebview)
        }
    }

    // virtual member
    var size :Pair<Int,Int>
        get() = Pair(width,height)
        set(v) { width = v.first; height = v.second }

    var uri : URI
        get() = URI(urlStr)
        set(v) {urlStr = v.toString()}
}