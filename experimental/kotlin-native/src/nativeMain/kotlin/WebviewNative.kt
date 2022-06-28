import webview.*
import kotlinx.cinterop.*

actual class WebviewNative {
    actual companion object {
        actual fun greeting() {
            println("Hello World from Native")
        }

    }
}