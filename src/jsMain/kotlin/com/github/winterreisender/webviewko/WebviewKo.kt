package com.github.winterreisender.webviewko
import kotlin.js.*

/**
 * The Kotlin/JS binding to webview
 *
 * @constructor create a webview or throws `Exception` if failed
 */
actual class WebviewKo actual constructor(debug: Int) {

    private var webview :dynamic = null

    init {
        js("""var webviewnode = require('webview-nodejs')""")
        webview = js("""new webviewnode.Webview(debug == 1);""")

    }

    /**
     * The window size hints used by `WebviewKo.size`
     *
     * A Wrapper of WEBVIEW_HINT_NONE, WEBVIEW_HINT_MIN, WEBVIEW_HINT_MAX and WEBVIEW_HINT_FIXED
     *
     */
    actual enum class WindowHint(v :Int) {
        None(0), Min(1), Max(2), Fixed(3)
    }

    /**
     * Updates the size of the native window.
     *
     * Accepts a WEBVIEW_HINT
     *
     * @param hints can be one of `WEBVIEW_HINT_NONE`, `WEBVIEW_HINT_MIN`, `WEBVIEW_HINT_MAX` or `WEBVIEW_HINT_FIXED`
     */
    actual fun size(width: Int, height: Int, hints: WindowHint) {
        webview.size(width,height,hints.ordinal)
    }

    /**
     * Should be used in [bind] to throw an exception in JS
     *
     * This exception will be caught by [bind] and trigger the `Promise.reject(reason)` in JS.
     *
     * @param reason the reason shown in JS.
     * @param json the JSON Exception object for JS. If it's not null, `reason` will have no effect.
     */
    actual class JSRejectException actual constructor(reason: String?, json: String?) : Throwable()

    /**
     * Binds a Kotlin callback so that it will appear under the given name as a global JS function.
     *
     * @param name the name of the global JS function
     * @param fn the callback function which receives the request parameter in JSON as input and return the response JSON. If you want to reject the `Promise`, throw [JSRejectException] in `fn`
     */
    actual fun bind(name: String, fn: WebviewKo.(String) -> String) {
        val fn :(dynamic, String?)->Any = { w, req ->
            kotlin.runCatching { fn(req ?: "") }.fold(
                onSuccess = { arrayOf<Any>(true,it) },
                onFailure =  {
                    when(it) {
                        is JSRejectException -> arrayOf<Any>(false,it.message ?: "")
                        else -> throw it
                    }
                }
            )
        }

        webview.bindRaw(name,fn);
    }

    /**
     * Posts a function to be executed on the main thread.
     *
     * It safely schedules the callback to be run on the main thread on the next main loop iteration.
     *
     * @param fn the function to be executed on the main thread.
     *
     */
    actual fun dispatch(fn: WebviewKo.() -> Unit) {
        webview.dispatch {_ -> fn() };
    }

    /**
     * Updates the title of the native window.
     *
     * Must be called from the UI thread.
     *
     * @param v the new title
     */
    actual fun title(v: String) {
        webview.title(v)
    }

    /**
     * Navigates webview to the given URL
     *
     * URL may be a data URI, i.e. "data:text/text,...". It is often ok not to url-encode it properly, webview will re-encode it for you. Same as [navigate]
     *
     * @param v the URL or URI
     * */
    actual fun url(v: String) =
        navigate(v)

    /**
     * Navigates webview to the given URL
     *
     * URL may be a data URI, i.e. "data:text/text,...". It is often ok not to url-encode it properly, webview will re-encode it for you. Same as [url]
     *
     * @param url the URL or URI
     * */
    actual fun navigate(url: String) {
        webview.navigate(url)
    }

    /**
     * Set webview HTML directly.
     *
     * @param v the HTML content
     */
    actual fun html(v: String) {
        webview.html(v)
    }

    /**
     * Injects JS code at the initialization of the new page.
     *
     * Same as `initJS`. Every time the webview will open a new page - this initialization code will be executed. It is guaranteed that code is executed before window.onload.
     *
     * @param js the JS code
     */
    actual fun init(js: String) {
        webview.init(js)
    }

    /**
     * Evaluates arbitrary JS code.
     *
     * Evaluation happens asynchronously, also the result of the expression is ignored. Use the `webview_bind` function if you want to receive notifications about the results of the evaluation.
     *
     * @param js the JS code
     */
    actual fun eval(js: String) {
        webview.eval(js)
    }

    /**
     * Removes a callback that was previously set by `webview_bind`.
     *
     * @param name the name of JS function used in `webview_bind`
     */
    actual fun unbind(name: String) {
        webview.unbind(name)
    }

    /**
     * Runs the main loop and destroy it when terminated.
     *
     * This will block the thread.
     */
    actual fun show() {
        webview.show()
    }

    /**
     * Stops the main loop.
     *
     * It is safe to call this function from another other background thread.
     *
     */
    actual fun terminate() {
        webview.terminate()
    }

}