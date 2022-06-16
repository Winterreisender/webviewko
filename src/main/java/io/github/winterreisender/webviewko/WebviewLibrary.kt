//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package io.github.winterreisender.webviewko

import com.sun.jna.*

interface WebviewLibrary : Library {
    fun webview_create(debug: Int, window: Pointer?): Pointer
    fun webview_destroy(webview: Pointer)
    fun webview_run(webview: Pointer)
    fun webview_terminate(webview: Pointer)
    fun webview_dispatch(webview: Pointer, dispatchFunction: webview_dispatch_fn_callback, args: Pointer)

    // Not mapped by webview_csharp
    // Returns a native window handle pointer. When using GTK backend the pointer
    // is GtkWindow pointer, when using Cocoa backend the pointer is NSWindow
    // pointer, when using Win32 backend the pointer is HWND pointer.
    /**
     * Not mapped by webview_csharp
     *
     * Returns a native window handle pointer. When using GTK backend the pointer
     * is GtkWindow pointer, when using Cocoa backend the pointer is NSWindow
     * pointer, when using Win32 backend the pointer is HWND pointer.
     */
    @Deprecated("Not suggested to use")
    fun webview_get_window(webview: Pointer): Pointer

    @Deprecated("")
    fun webview_set_title(vebview: Pointer, title: Pointer)
    fun webview_set_title(vebview: Pointer, title: String)
    fun webview_set_size(vebview: Pointer, width: Int, height: Int, hints: Int)
    //	@Deprecated
    //	void webview_get_size(Pointer var1, IntByReference var2, IntByReference var3, IntByReference var4, IntByReference var5, IntByReference var6);
    //
    //	void webview_get_bounds(Pointer var1, IntBuffer var2, IntBuffer var3, IntBuffer var4, IntBuffer var5, IntBuffer var6);

    @Deprecated("")
    fun webview_navigate(vebview: Pointer, url: Pointer)
    fun webview_navigate(vebview: Pointer, url: String)

    @Deprecated("")
    fun webview_init(webview: Pointer, js: Pointer)
    fun webview_init(webview: Pointer, js: String)

    @Deprecated("")
    fun webview_eval(webview: Pointer, js: Pointer)
    fun webview_eval(webview: Pointer, js: String)

    @Deprecated("")
    fun webview_bind(webview: Pointer, name: Pointer, callback: webview_bind_fn_callback, args: Pointer)
    fun webview_bind(webview: Pointer, name: String, callback: webview_bind_fn_callback, args: Pointer)

    @Deprecated("")
    fun webview_return(webview: Pointer, id: Pointer, result: Int, resultJson: Pointer)
    fun webview_return(webview: Pointer, id: String, result: Int, resultJson: String)
    interface webview_bind_fn_callback : Callback {
        fun apply(id: Pointer, req: Pointer, args: Pointer? = null)
    }

    interface webview_dispatch_fn_callback : Callback {
        fun apply(id: Pointer, req: Pointer, args: Pointer? = null)
    }

}


object Webview {
    const val JNA_LIBRARY_NAME = "webview"
    val JNA_NATIVE_LIB = NativeLibrary.getInstance("webview")
    val INSTANCE: WebviewLibrary = Native.load("webview", WebviewLibrary::class.java)

    // Window size hints
    // IN JNA layer, better to use const val instead of enum class
    const val WEBVIEW_HINT_NONE = 0 // Width and height are default size
    const val WEBVIEW_HINT_MIN = 1  // Width and height are minimum bounds
    const val WEBVIEW_HINT_MAX = 2  // Width and height are maximum bounds
    const val WEBVIEW_HINT_FIXED = 3 // Window size can not be changed by a user

    @Deprecated("", ReplaceWith("WEBVIEW_HINT_XXX"))
    enum class WindowHint(val value :Int) {
        WEBVIEW_HINT_NONE(0), // Width and height are default size
        WEBVIEW_HINT_MIN(1),  // Width and height are minimum bounds
        WEBVIEW_HINT_MAX(2),  // Width and height are maximum bounds
        WEBVIEW_HINT_FIXED(3) // Window size can not be changed by a user
    }

}