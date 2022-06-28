import webview.*

fun main() {
    println("Test1")
    val w: webview_t? = webview_create(1,null)
    webview_navigate(w,"https://example.com")
    webview_run(w)
    webview_destroy(w)
}