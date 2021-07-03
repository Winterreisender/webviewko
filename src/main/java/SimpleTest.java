import com.sun.jna.Pointer;
import webview.WebviewLibrary;
import webview.WebviewThread;

public class SimpleTest {

    public static void main(String[] args) {

        System.out.println("0");
        WebviewLibrary webViewLib = WebviewLibrary.INSTANCE;
        System.out.println("1");
        Pointer windowPointer = webViewLib.webview_create(0, null);
        System.out.println("2");
        webViewLib.webview_set_title(windowPointer, "Hello");
        System.out.println("3");
        webViewLib.webview_set_size(windowPointer, 800, 600, WebviewLibrary.WEBVIEW_HINT_NONE);
        System.out.println("4");
        webViewLib.webview_navigate(windowPointer, "https://en.m.wikipedia.org/wiki/Main_Page");
        System.out.println("5");

//        WebviewThread t1 = new WebviewThread(webViewLib, windowPointer);
//        t1.start();
        System.out.println("5b");

        webViewLib.webview_run(windowPointer);
        System.out.println("77");
//        WebviewThread t2 = new WebviewThread();
//        t2.start();
        boolean run = true;
        while (run) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                run = false;
            }
        }

    }
}
