package webview;

import com.sun.jna.Pointer;

public class WebviewThread extends Thread {

    private final Pointer windowPointer;
    private final WebviewLibrary webViewLib;

    public WebviewThread(WebviewLibrary webViewLib, Pointer windowPointer) {
        this.webViewLib = webViewLib;
        this.windowPointer = windowPointer;
    }

    @Override
    public void run() {
        System.out.println("7");
        webViewLib.webview_run(windowPointer);
        System.out.println("8");
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
