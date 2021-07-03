import com.sun.jna.Pointer;
import webview.WebviewLibrary;

public class SimpleTest {

    public static void main(String[] args) {
        System.out.println("0");
        WebviewLibrary webViewLib = WebviewLibrary.INSTANCE;
        System.out.println("1");
        Pointer windowPointer = webViewLib.webview_create(0, null);
        System.out.println("2");
        webViewLib.webview_set_title(windowPointer, "Hello");
        System.out.println("3");
        webViewLib.webview_set_bounds(windowPointer, 50, 50, 500, 500, 0);
        System.out.println("4");
        webViewLib.webview_navigate(windowPointer, "https://en.m.wikipedia.org/wiki/Main_Page");
        System.out.println("5");
        webViewLib.webview_run(windowPointer);
        System.out.println("6");
        boolean run = true;
        while (run) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                run = false;
            }
        }
        webViewLib.webview_destroy(windowPointer);
    }
}
