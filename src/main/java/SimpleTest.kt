import webview.WebviewLibrary

// TODO: use Gradle Test https://kotlinlang.org/docs/jvm-test-using-junit.html#create-a-test

fun main(args: Array<String>) {
    println("0")
    val webViewLib = WebviewLibrary.INSTANCE
    println("1")
    val windowPointer = webViewLib.webview_create(0, null)
    println("2")
    webViewLib.webview_set_title(windowPointer, "Hello")
    println("3")
    webViewLib.webview_set_size(windowPointer, 800, 600, WebviewLibrary.WEBVIEW_HINT_NONE)
    println("4")
    webViewLib.webview_navigate(windowPointer, "https://www.whatsmybrowser.org/")
    println("5")

//        WebviewThread t1 = new WebviewThread(webViewLib, windowPointer);
//        t1.start();
    println("5b")
    webViewLib.webview_run(windowPointer)
    println("77")
    //        WebviewThread t2 = new WebviewThread();
//        t2.start();
    var run = true
    while (run) {
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
            run = false
        }
    }
}