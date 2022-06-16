//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package webview

import com.sun.jna.*

interface WebviewLibrary : Library {
    fun webview_create(var1: Int, var2: Pointer?): Pointer
    fun webview_destroy(var1: Pointer)
    fun webview_run(var1: Pointer)
    fun webview_terminate(var1: Pointer)
    fun webview_dispatch(var1: Pointer, var2: webview_dispatch_fn_callback, var3: Pointer)
    fun webview_get_window(var1: Pointer): Pointer

    @Deprecated("")
    fun webview_set_title(var1: Pointer, var2: Pointer)
    fun webview_set_title(var1: Pointer, var2: String)
    fun webview_set_size(var1: Pointer, width: Int, height: Int, hints: Int)
    //	@Deprecated
    //	void webview_get_size(Pointer var1, IntByReference var2, IntByReference var3, IntByReference var4, IntByReference var5, IntByReference var6);
    //
    //	void webview_get_bounds(Pointer var1, IntBuffer var2, IntBuffer var3, IntBuffer var4, IntBuffer var5, IntBuffer var6);

    @Deprecated("")
    fun webview_navigate(var1: Pointer, var2: Pointer)
    fun webview_navigate(var1: Pointer, var2: String)

    @Deprecated("")
    fun webview_init(var1: Pointer, var2: Pointer)
    fun webview_init(var1: Pointer, var2: String)

    @Deprecated("")
    fun webview_eval(var1: Pointer, var2: Pointer)
    fun webview_eval(var1: Pointer, var2: String)

    @Deprecated("")
    fun webview_bind(var1: Pointer, var2: Pointer, var3: webview_bind_fn_callback, var4: Pointer)
    fun webview_bind(var1: Pointer, var2: String, var3: webview_bind_fn_callback, var4: Pointer)

    @Deprecated("")
    fun webview_return(var1: Pointer, var2: Pointer, var3: Int, var4: Pointer)
    fun webview_return(var1: Pointer, var2: String, var3: Int, var4: String)
    interface webview_bind_fn_callback : Callback {
        fun apply(var1: Pointer, var2: Pointer)
    }

    interface webview_dispatch_fn_callback : Callback {
        fun apply(var1: Pointer, var2: Pointer)
    }

    // TODO: Decouple the companion object
    companion object {
        const val JNA_LIBRARY_NAME = "webview"
        val JNA_NATIVE_LIB = NativeLibrary.getInstance("webview")
        val INSTANCE = Native.loadLibrary("webview", WebviewLibrary::class.java)

        // Window size hints
        const val WEBVIEW_HINT_NONE = 0 // Width and height are default size
        const val WEBVIEW_HINT_MIN = 1 // Width and height are minimum bounds
        const val WEBVIEW_HINT_MAX = 2 // Width and height are maximum bounds
        const val WEBVIEW_HINT_FIXED = 3 // Window size can not be changed by a user
    }
}
