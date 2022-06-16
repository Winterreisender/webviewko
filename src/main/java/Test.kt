import io.github.winterreisender.webviewko.Webview
import org.junit.jupiter.api.Test

internal class Test {
    @Test fun simpleTest() {
        val webViewLib = Webview.INSTANCE
        val windowPointer = webViewLib.webview_create(0, null)
        webViewLib.webview_set_title(windowPointer, "Hello")
        webViewLib.webview_set_size(windowPointer, 800, 600, Webview.WEBVIEW_HINT_NONE)
        webViewLib.webview_navigate(windowPointer, "https://www.whatsmybrowser.org/")
        webViewLib.webview_run(windowPointer)
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