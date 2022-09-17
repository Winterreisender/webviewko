package com.github.winterreisender.webviewko

import com.sun.jna.Native
import com.sun.jna.ptr.PointerByReference
import java.awt.*
import java.io.Serial
import java.util.function.Consumer

// Copied and modified from Casterlabs/Webview
// TODO: Kotlin-fy it
// TODO: Compose support
/**
 * Swing/AWT support
 * */
class WebviewKoAWT(private val debug: Int, private val libPath :String? = null, private val onCreate: Consumer<WebviewKo>) :Canvas() {
    private var initialized = false
    private var webview: WebviewKo? = null
    private var lastSize: Dimension? = null
    private val isWin32: Boolean = System.getProperty("os.name").lowercase().contains("windows")

    override fun paint(g: Graphics?) {
        val size = this.size
        if (size != lastSize) {
            lastSize = size
            if (webview != null) {
                this.updateSize()
            }
        }
        if (!initialized) {
            initialized = true
            Thread {
                webview = WebviewKo(debug, libPath, PointerByReference(Native.getComponentPointer(this)))
                this.updateSize()
                onCreate.accept(webview!!)
            }.start()
        }
    }

    private fun updateSize() {
        var width = lastSize!!.width
        var height = lastSize!!.height

        // There is a random margin on Windows that isn't visible, so we must
        // compensate.
        // TODO figure out why this is caused.
        // TODO: Check DPI
        if (isWin32) {
            width -= 16
            height -= 39
        }
        webview?.size(width, height, WebviewKo.WindowHint.Fixed)
    }

    fun dispatch(fn :WebviewKo.()->Unit) = webview!!.dispatch(fn)

    companion object {
        @Serial
        private const val serialVersionUID: Long = -6551398642418050882L
    }
}

