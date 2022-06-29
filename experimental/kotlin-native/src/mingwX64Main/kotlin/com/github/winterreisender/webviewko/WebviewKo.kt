package com.github.winterreisender.webviewko

import kotlinx.cinterop.*
import webview.*
import kotlin.native.concurrent.freeze


/**
 * The High level binding to webview in Kotlin
 */

actual class WebviewKo actual constructor(debug: Int) {
    private val w :webview_t? = webview_create(debug, null)

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


    actual enum class WindowHint {
        None, Min, Max, Fixed
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
     * Injects JavaScript code at the initialization of the new page.
     *
     * Same as `initJS`. Every time the webview will open a new page - this initialization code will be executed. It is guaranteed that code is executed before window.onload.
     *
     * @param js the JS code
     */
    actual fun init(js: String) = webview_init(w,js)

    /**
     * Evaluates arbitrary JavaScript code.
     *
     * Evaluation happens asynchronously, also the result of the expression is ignored. Use the `webview_bind` function if you want to receive notifications about the results of the evaluation.
     *
     * @param js the JS code
     */
    actual fun eval(js: String) = webview_eval(w,js)

    /**
     * Binds a native Kotlin/Java callback so that it will appear under the given name as a global JavaScript function.
     *
     * Callback receives a request string. Request string is a JSON array of all the arguments passed to the JavaScript function.
     *
     * @param name the name of the global JavaScript function
     * @param fn the callback function which receives the request parameter in JSON as input and return the response to JS in JSON. In Java the fn should be String response(WebviewKo webview, String request)
     */
    actual fun bind(name: String, fn: WebviewKo.(String?) -> String) {
        val ctx = BindContext(w,{fn(it)})
        ctx.freeze()
        webview_bind(
            w,
            name,
            staticCFunction { seq,req,arg ->
                val c = arg!!.asStableRef<BindContext>().get()
                val r = c.callback(req?.toKString())
                webview_return(c.webview,seq?.toKString(),0,r)
            },
            StableRef.create(ctx).asCPointer()
        )
    }

    class BindContext(
        val webview :webview_t? = null,
        val callback :(String?)->String? = {null}
    )

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
        val ctx = DispatchContext(this,fn)
        ctx.freeze()
        webview_dispatch(
            w,
            staticCFunction { w,arg ->
                val c = arg!!.asStableRef<DispatchContext>().get()
                c.callback(c.webviewKo)
            },
            StableRef.create(ctx).asCPointer()
        )
    }
    class DispatchContext(
        val webviewKo: WebviewKo,
        val callback : WebviewKo.() ->Unit = {}
    )

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

}