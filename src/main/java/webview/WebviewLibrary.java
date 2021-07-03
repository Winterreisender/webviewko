//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package webview;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import java.nio.IntBuffer;

public interface WebviewLibrary extends Library {
	String JNA_LIBRARY_NAME = "webview";
	NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance("webview");
	WebviewLibrary INSTANCE = (WebviewLibrary)Native.loadLibrary("webview", WebviewLibrary.class);

	Pointer webview_create(int var1, Pointer var2);

	void webview_destroy(Pointer var1);

	void webview_run(Pointer var1);

	void webview_terminate(Pointer var1);

	void webview_dispatch(Pointer var1, WebviewLibrary.webview_dispatch_fn_callback var2, Pointer var3);

	Pointer webview_get_window(Pointer var1);

	/** @deprecated */
	@Deprecated
	void webview_set_title(Pointer var1, Pointer var2);

	void webview_set_title(Pointer var1, String var2);

	void webview_set_size(Pointer var1, int var2, int var3, int var4, int var5, int var6);

	/** @deprecated */
//	@Deprecated
//	void webview_get_size(Pointer var1, IntByReference var2, IntByReference var3, IntByReference var4, IntByReference var5, IntByReference var6);
//
//	void webview_get_bounds(Pointer var1, IntBuffer var2, IntBuffer var3, IntBuffer var4, IntBuffer var5, IntBuffer var6);

	/** @deprecated */
	@Deprecated
	void webview_navigate(Pointer var1, Pointer var2);

	void webview_navigate(Pointer var1, String var2);

	/** @deprecated */
	@Deprecated
	void webview_init(Pointer var1, Pointer var2);

	void webview_init(Pointer var1, String var2);

	/** @deprecated */
	@Deprecated
	void webview_eval(Pointer var1, Pointer var2);

	void webview_eval(Pointer var1, String var2);

	/** @deprecated */
	@Deprecated
	void webview_bind(Pointer var1, Pointer var2, WebviewLibrary.webview_bind_fn_callback var3, Pointer var4);

	void webview_bind(Pointer var1, String var2, WebviewLibrary.webview_bind_fn_callback var3, Pointer var4);

	/** @deprecated */
	@Deprecated
	void webview_return(Pointer var1, Pointer var2, int var3, Pointer var4);

	void webview_return(Pointer var1, String var2, int var3, String var4);

	public interface webview_bind_fn_callback extends Callback {
		void apply(Pointer var1, Pointer var2);
	}

	public interface webview_dispatch_fn_callback extends Callback {
		void apply(Pointer var1, Pointer var2);
	}
}
