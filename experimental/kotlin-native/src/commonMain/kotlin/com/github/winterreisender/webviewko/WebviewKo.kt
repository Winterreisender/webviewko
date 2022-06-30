package com.github.winterreisender.webviewko

/**
 * The High level binding to webview in Kotlin
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
     * Injects JavaScript code at the initialization of the new page.
     *
     * Same as `initJS`. Every time the webview will open a new page - this initialization code will be executed. It is guaranteed that code is executed before window.onload.
     *
     * @param js the JS code
     */
    fun init(js :String)

    /**
     * Evaluates arbitrary JavaScript code.
     *
     * Evaluation happens asynchronously, also the result of the expression is ignored. Use the `webview_bind` function if you want to receive notifications about the results of the evaluation.
     *
     * @param js the JS code
     */
    fun eval(js :String)


    /**
     * Binds a native Kotlin/Java callback so that it will appear under the given name as a global JavaScript function.
     *
     * Callback receives a request string. Request string is a JSON array of all the arguments passed to the JavaScript function.
     *
     * @param name the name of the global JavaScript function
     * @param fn the callback function which receives the request parameter in JSON as input and return the response to JS in JSON. In Java the fn should be String response(WebviewKo webview, String request)
     */
    fun bind(name :String, fn : WebviewKo.(String?)->String)

    /**
     * Removes a callback that was previously set by `webview_bind`.
     *
     * @param name the name of JS function used in `webview_bind`
     */
    fun unbind(name: String)


    /**
     * Posts a function to be executed on the main thread.
     *
     * It safely schedules the callback to be run on the main thread on the next main loop iteration. Like `invokeLater` in Swing
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