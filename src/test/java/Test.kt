import com.github.winterreisender.webviewko.WebviewJNA
import com.github.winterreisender.webviewko.WebviewKo
import com.github.winterreisender.webviewko.WindowHint
import java.awt.Desktop
import kotlin.test.Test
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertNotNull

internal class Test {
    @Test fun copyDllTest() {
        return

        println(this::class.java.classLoader.getResource("WebView2Loader.dll"))
        assertNotNull(this::class.java.classLoader.getResourceAsStream("WebView2Loader.dll"))
        try {
            Files.copy(this::class.java.classLoader.getResourceAsStream("WebView2Loader.dll")!!, Path.of("${System.getProperty("user.dir")}/WebView2Loader.dll"))
        }catch (e :FileAlreadyExistsException) {
            //println("FileAlreadyExistsException")
        }
    }
    @Test fun webviewKoTest() {
        return

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
        return

        with(WebviewJNA.getJNALibrary()) {
            val pWebview = WebviewJNA.getJNALibrary().webview_create(0, null)
            webview_set_title(pWebview, "Hello")
            webview_set_size(pWebview, 800, 600, WebviewJNA.WEBVIEW_HINT_NONE)
            webview_navigate(pWebview, "https://www.whatsmybrowser.org/")
            webview_run(pWebview)
            webview_destroy(pWebview)
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