import com.github.winterreisender.webviewko.WebviewKo
import kotlin.test.Test
import kotlin.test.assertTrue

// https://kotlinlang.org/docs/js-interop.html#declare-static-members-of-a-class

@JsModule("webview-nodejs")
@JsNonModule
external class Webview

internal class TestKt {
    @Test fun test0() {
        val p = 0
        js("""var ffi = require('ffi-napi');""")
        js("""var msvcrt = new ffi.Library('msvcrt.dll', {'ceil': [ 'double', [ 'double' ] ]});""")
        assertTrue(js(""" typeof msvcrt.ceil === 'function' """) as Boolean)
        assertTrue(js(""" msvcrt.ceil(100.9) === 101 """) as Boolean)
        js("""p=msvcrt.ceil(10.9);""")
        println(p)
    }

    @Test fun test1() {
        js("""console.log( process.cwd())""")
        js("""var ffi = require('ffi-napi');""")
        js("""var webview = new ffi.Library('webview.dll', { 
            'webview_create'  : [ 'pointer', [ 'int', 'pointer' ] ],
            'webview_run'     : [ 'void'   , [ 'pointer' ] ],
            'webview_destroy' : [ 'void'   , [ 'pointer' ] ],
            'webview_navigate': [ 'void'   , [ 'pointer', 'string' ] ],
        });""")
        assertTrue(js(""" typeof webview.webview_create === 'function' """) as Boolean)
        val w :dynamic = 0L

        js("""w = webview.webview_create(0,null)""")
        js("""webview.webview_navigate(w,'https://example.com')""")
        js("""webview.webview_run(w)""")
        js("""webview.webview_destroy(w)""")
    }

    @Test fun test2() {
        WebviewKo(1).run {
            title("Nodejs")
            size(800,600)
            navigate("https://example.com")
            start()
        }

    }

    @Test fun test3() {
        val x = Webview()
    }
}