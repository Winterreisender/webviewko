import webview.*
import kotlin.test.Test

class TestNative {
    @Test
    fun test0() {
        val w : webview_t? = webview_create(0,null)
        webview_set_size(w, 800, 600, 0)
        webview_navigate(w, "https://example.com")
        webview_run(w)
        webview_destroy(w)
    }

    @Test fun test1() {

    }


}