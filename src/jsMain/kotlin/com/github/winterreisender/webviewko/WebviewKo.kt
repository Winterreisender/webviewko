package com.github.winterreisender.webviewko

/**
 * The Kotlin/JS binding to webview
 *
 * @constructor create a webview or throws `Exception` if failed
 */
actual class WebviewKo actual constructor(debug: Int) {

    /**
     * Binds a Kotlin callback so that it will appear under the given name as a global JS function.
     *
     * Callback `fn` receives a request String, which is a JSON array of all the arguments passed to the JS function and returns `Unit`,`String` or `Pair<String,Int>`.
     *
     * @param name the name of the global JS function
     * @param fn the callback function which receives the request parameter in JSON as input and return the response JSON and status.When `fn` return `Pair(Response,Status)` the webview will receive the response and status . When `fn` returns `String`, the Status is 0. When `fn` returns `Unit`, the webview won't receive a feedback.
     */

    private var ffi :dynamic = null
    private var lib :dynamic = null
    private var webview :dynamic = null

    init {
        js("""this.ffi = require('ffi-napi');""")
        js("""this.lib = new this.ffi.Library('webview.dll', { 
            'webview_create'   : [ 'pointer', [ 'int', 'pointer' ] ],
            'webview_run'      : [ 'void'   , [ 'pointer' ] ],
            'webview_terminate': [ 'void'   , [ 'pointer' ] ],
            'webview_destroy'  : [ 'void'   , [ 'pointer' ] ],
            'webview_set_title': [ 'void'   , [ 'pointer', 'string' ] ],
            'webview_set_html' : [ 'void'   , [ 'pointer', 'string' ] ],
            'webview_navigate' : [ 'void'   , [ 'pointer', 'string' ] ],
            'webview_init'     : [ 'void'   , [ 'pointer', 'string' ] ],
            'webview_eval'     : [ 'void'   , [ 'pointer', 'string' ] ],
            'webview_dispatch' : [ 'void'   , [ 'pointer', 'pointer'] ],
            'webview_bind'     : [ 'void'   , [ 'pointer', 'string', 'pointer', 'pointer' ] ],
            'webview_return'   : [ 'void'   , [ 'pointer', 'string', 'int', 'string' ] ],
            'webview_unbind'   : [ 'void'   , [ 'pointer', 'string' ] ],
            'webview_set_size' : [ 'void'   , [ 'pointer', 'int', 'int', 'int' ] ],
        });""")
        js("""this.webview = this.lib.webview_create(debug,null)""")
    }

    /**
     * Updates the title of the native window.
     *
     * Must be called from the UI thread.
     *
     * @param v the new title
     */
    actual fun title(v: String) {
        js("""this.lib.webview_set_title(this.webview,v)""")
    }

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
     * @param url the URL or URI
     * */
    actual fun navigate(url: String) {
        js("""this.lib.webview_navigate(this.webview,url)""")
    }

    /**
     * Set webview HTML directly.
     *
     * @param v the HTML content
     */
    actual fun html(v: String) {
        js("""this.lib.webview_set_html(this.webview,v)""")
    }

    /**
     * Updates the size of the native window.
     *
     * Accepts a WEBVIEW_HINT
     *
     * @param hints can be one of `WEBVIEW_HINT_NONE`, `WEBVIEW_HINT_MIN`, `WEBVIEW_HINT_MAX` or `WEBVIEW_HINT_FIXED`
     */
    actual fun size(width: Int, height: Int, hints: WindowHint) {
        val hintsJS = hints.ordinal
        js("""this.lib.webview_set_size(this.webview,width,height,hintsJS)""")
    }

    /**
     * The window size hints used by `WebviewKo.size`
     *
     * A Wrapper of WEBVIEW_HINT_NONE, WEBVIEW_HINT_MIN, WEBVIEW_HINT_MAX and WEBVIEW_HINT_FIXED
     *
     */
    actual enum class WindowHint {
        None, Min, Max, Fixed
    }

    /**
     * Injects JS code at the initialization of the new page.
     *
     * Every time the webview will open a new page - this initialization code will be executed. It is guaranteed that code is executed before window.onload.
     *
     * @param js the JS code
     */
    actual fun init(js: String) {
        js("""this.lib.webview_init(this.webview,js)""")
    }

    /**
     * Evaluates arbitrary JS code.
     *
     * Evaluation happens asynchronously, also the result of the expression is ignored. Use the `webview_bind` function if you want to receive notifications about the results of the evaluation.
     *
     * @param js the JS code
     */
    actual fun eval(js: String) {
        js("""this.lib.webview_eval(this.webview,js)""")
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
        val fn :(req :String?)->Pair<String,Int> = {
            kotlin.runCatching { fn(it ?: "") }.fold(
                onSuccess = { Pair(it, 0) },
                onFailure =  {
                    when(it) {
                        is JSRejectException -> Pair(it.message ?: "", 1)
                        else -> throw it
                    }
                }
            )
        }

        js("""var resolve = (function (seq,result,isError) { this.lib.webview_return(this.webview,seq,isError,result); }).bind(this); """) //TODO: webview_return not work
        js("""var callback = this.ffi.Callback('void',['string','string','pointer'], function(seq,req,arg) {
            var resultAndError = fn(req);
            var result = resultAndError.first
            var isError = resultAndError.second
            resolve(seq,result,isError);
        });""")

        js("""this.lib.webview_bind(this.webview, name, callback, null)""")
        js("""process.on('exit', function() { callback; })""") // avoid GC

    }

    /**
     * Removes a callback that was previously set by `webview_bind`.
     *
     * @param name the name of JS function used in `webview_bind`
     */
    actual fun unbind(name: String) {
        js("""this.lib.webview_unbind(this.webview,name)""")
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
        val fn :(dynamic,dynamic)->Unit = { _,_ -> fn() }
        js("""callback = this.ffi.Callback('void',['pointer','pointer'], fn);""")
        js("""this.lib.webview_dispatch(this.webview,callback)""")
        js("""process.on('exit', function() { callback; })""") // avoid GC
    }

    /**
     * Runs the main loop and destroy it when terminated.
     *
     * This will block the thread.
     */
    actual fun show() {
        js("""this.lib.webview_run(this.webview)""")
        js("""this.lib.webview_destroy(this.webview)""")
    }

    /**
     * Stops the main loop.
     *
     * It is safe to call this function from another other background thread.
     *
     */
    actual fun terminate() {
        js("""this.lib.webview_terminate(this.webview)""")
    }
}