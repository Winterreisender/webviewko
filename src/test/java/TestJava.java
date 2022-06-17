import com.github.winterreisender.webviewko.WebviewKo;
import com.github.winterreisender.webviewko.WindowHint;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;


public class TestJava {
    @Test
    void testSimpleJava() throws URISyntaxException, MalformedURLException {
        WebviewKo webview = new WebviewKo();

        webview.setTitle("webviewKo Java Test");
        webview.setWidth(1024);
        webview.setWidth(768);
        webview.setUri(new URI("https://bing.com"));
        webview.setWindowHint(WindowHint.None);

        webview.show();
    }
}
