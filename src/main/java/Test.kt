import io.github.winterreisender.webviewko.WebviewJNA
import io.github.winterreisender.webviewko.WebviewKo
import io.github.winterreisender.webviewko.WindowHint
import org.junit.jupiter.api.Test
import java.net.URI

internal class Test {
    @Test fun webviewKoTest() {
        val webview = WebviewKo().apply {
            title = "webviewKo Test"
            size = Pair(1024,768)
            //urlStr = "https://www.whatsmybrowser.org/"
            uri = URI("https://www.whatsmybrowser.org/") // Both urlStr and url is OK
            windowHint = WindowHint.None
        }
        webview.show()
    }
    @Test fun jnaLevelTest() {
        with(WebviewJNA.INSTANCE) {
            val pWindow = WebviewJNA.INSTANCE.webview_create(0, null)
            webview_set_title(pWindow, "Hello")
            webview_set_size(pWindow, 800, 600, WebviewJNA.WEBVIEW_HINT_NONE)
            webview_navigate(pWindow, "https://www.whatsmybrowser.org/")
            webview_run(pWindow)
            webview_destroy(pWindow)
        }
    }

}


// fun simpleTest0() {
//     val webViewLib = Webview.INSTANCE
//     val windowPointer = webViewLib.webview_create(0, null)
//     webViewLib.webview_set_title(windowPointer, "Hello")
//     webViewLib.webview_set_size(windowPointer, 800, 600, Webview.WEBVIEW_HINT_NONE)
//     webViewLib.webview_navigate(windowPointer, "https://www.whatsmybrowser.org/")
//
//     //        WebviewThread t1 = new WebviewThread(webViewLib, windowPointer);
//     //        t1.start();
//     webViewLib.webview_run(windowPointer)
//     //        WebviewThread t2 = new WebviewThread();
//     //        t2.start();
//     var run = true
//     while (run) {
//         try {
//             Thread.sleep(1000)
//         } catch (e: InterruptedException) {
//             e.printStackTrace()
//             run = false
//         }
//     }
// }