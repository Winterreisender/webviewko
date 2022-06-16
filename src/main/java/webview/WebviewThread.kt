package webview

import com.sun.jna.Pointer

class WebviewThread(private val webViewLib: WebviewLibrary, private val windowPointer: Pointer) : Thread() {
    override fun run() {
        println("7")
        webViewLib.webview_run(windowPointer)
        println("8")
        var run = true
        while (run) {
            try {
                sleep(1000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
                run = false
            }
        }
        webViewLib.webview_destroy(windowPointer)
    }
}