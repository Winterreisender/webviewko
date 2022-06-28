import kotlin.test.Test
import kotlinx.cinterop.*
import platform.windows.*
import webview.*


class Test0 {
    @Test fun test1() {

        println("Test1")
        var w: webview_t? = null;
        w = webview_create(1,null)
        webview_navigate(w,"https://example.com")
        webview_run(w)
        webview_destroy(w)
    }

    @Test fun win32t() {
        memScoped {

            val hInstance = (GetModuleHandle!!)(null)
            val lpszClassName = "GijSoft"

            // In order to be able to create a window you need to have a window class available. A window class can be created for your
            // application by registering one. The following struct declaration and fill provides details for a new window class.
            val wc = alloc<WNDCLASSEX>();

            wc.cbSize        = sizeOf<WNDCLASSEX>().toUInt();
            wc.style         = 0u;
            wc.cbClsExtra    = 0;
            wc.cbWndExtra    = 0;
            wc.hInstance     = hInstance;
            wc.hIcon         = null;
            wc.hCursor       = (LoadCursor!!)(hInstance, IDC_ARROW);
            //wc.hbrBackground = HBRUSH(COLOR_WINDOW+1);
            wc.lpszMenuName  = null;
            wc.lpszClassName = lpszClassName.wcstr.ptr
            wc.hIconSm       = null;

            // This function actually registers the window class. If the information specified in the 'wc' struct is correct,
            // the window class should be created and no error is returned.
            if((RegisterClassEx!!)(wc.ptr) == 0u.toUShort())
            {
                println("Failed to register!")
                return
            }

            // This function creates the first window. It uses the window class registered in the first part, and takes a title,
            // style and position/size parameters. For more information about style-specific definitions, refer to the MSDN where
            // extended documentation is available.
            val hwnd = CreateWindowExA(WS_EX_CLIENTEDGE, lpszClassName, "Win32 C Window application by evolution536",
                (WS_OVERLAPPED or WS_CAPTION or WS_SYSMENU or WS_MINIMIZEBOX).toUInt(),
                CW_USEDEFAULT, CW_USEDEFAULT, 320, 125, null, null, hInstance, NULL
            )

            // Everything went right, show the window including all controls.
            ShowWindow(hwnd, 1);
            UpdateWindow(hwnd);


            // This part is the "message loop". This loop ensures the application keeps running and makes the window able to receive messages
            // in the WndProc function. You must have this piece of code in your GUI application if you want it to run properly.
            val Msg = alloc<MSG>();
            while((GetMessage!!)(Msg.ptr, null, 0u, 0u) > 0)
            {
                TranslateMessage(Msg.ptr);
                (DispatchMessage!!)(Msg.ptr);
            }
        }
    }
}