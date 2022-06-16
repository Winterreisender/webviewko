# WebviewKo

WebviewKo is an **experimental** project for now to bind webview with Kotlin and JNA for both Java and Kotlin, based on wiverson/webviewjar

## Help

### Kotlin API

for example:

```kotlin
import io.github.winterreisender.webviewko.WebviewJNA
import io.github.winterreisender.webviewko.WebviewKo
import io.github.winterreisender.webviewko.WindowHint
import java.net.URI

val webview = WebviewKo().apply {
   title = "webviewKo Test"
   size = Pair(1024,768)
   uri = URI("https://example.com/") // Both urlStr and url is OK
   windowHint = WindowHint.None
}
webview.show()
```

### Java API

for example:

```java
import io.github.winterreisender.webviewko.WebviewKo;
import io.github.winterreisender.webviewko.WindowHint;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

class Example {
    public static void main(String args[]) {
       WebviewKo webview = new WebviewKo();

       webview.setTitle("webviewKo Java Test");
       webview.setWidth(1024);
       webview.setWidth(768);
       webview.setUri(new URI("example.com"));
       webview.setWindowHint(WindowHint.None);

       webview.show();
    }
}
```

### CLI

```shell
Usage: webviewko options_list
Arguments: 
    uri -> URI/URL { String }
Options: 
    --width [800] -> window width in px { Int }
    --height [600] -> window height in px { Int }
    --title, -t [webviewko] -> window title { String }
    --help, -h -> Usage info 
```

for example:

```shell
java -jar webviewko.jar https://example.com
java -jar webviewko.jar https://example.com --title Hello --width 800 --height 600
```

for more information see GitHub Wiki

## Contribution

TODO

see GitHub Discussion

## Credits

| Project                                                       | License                                                                                          |
|---------------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| [wiverson/webviewjar](https://github.com/wiverson/webviewjar) | [MIT](https://github.com/wiverson/webviewjar/blob/master/LICENSE)                                |
| [webview_csharp](https://github.com/webview/webview_csharp)   | [MIT](https://github.com/webview/webview_csharp/blob/master/LICENSE)                             |
| [webview](https://github.com/webview/webview)                 | [MIT](https://github.com/webview/webview/blob/master/LICENSE)                                    |
| [JNA](https://github.com/java-native-access/jna)              | [LGPL-2.1-or-later OR Apache-2.0](https://github.com/java-native-access/jna/blob/master/LICENSE) |
| [Kotlin & kotlinx](https://kotlinlang.org/)                   | [Apache-2.0](https://github.com/JetBrains/kotlin/blob/master/LICENSE)                            |

## License

Apache-2.0
