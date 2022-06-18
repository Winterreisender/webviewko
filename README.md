# webviewko

![Top language](https://img.shields.io/github/languages/top/Winterreisender/webviewko?color=b99bf8&logo=kotlin)
![Java](https://img.shields.io/badge/Java-11,17-orange?logo=java)
![license](https://img.shields.io/github/license/Winterreisender/webviewko)

![dev release](https://img.shields.io/github/v/release/Winterreisender/webviewko?label=dev&include_prereleases)
[![Gradle CI MacOS](https://github.com/Winterreisender/webviewko/actions/workflows/gradle-ci.yml/badge.svg)](https://github.com/Winterreisender/webviewko/actions/workflows/gradle-ci.yml)
![GitHub last commit](https://img.shields.io/github/last-commit/Winterreisender/webviewko)
[![JitPack](https://jitpack.io/v/Winterreisender/webviewko.svg)](https://jitpack.io/#Winterreisender/webviewko)

webviewko is an **experimental** project to bind [webview](https://github.com/webview/webview) with Kotlin and JNA for both Java and Kotlin, based on wiverson/webviewjar.  
[webview](https://github.com/webview/webview) is a tiny cross-platform webview library.

![screenshot](screenshot.jpg)

## Usage

**See [GitHub Wiki](https://github.com/Winterreisender/webviewko/wiki) for the full document**

### Import

For Gradle (Kotlin DSL) :

```kotlin
repositories {
    ...
    maven("https://jitpack.io")
}

dependencies {
    ...
    implementation("com.github.Winterreisender:webviewko:main-SNAPSHOT")
}
```

### Kotlin API

```kotlin
import com.github.winterreisender.webviewko.*

with(WebviewKo()) {
    title("Title")
    size(800, 600)
    initJS("""console.log("Hello, from init")""")

    url("https://example.com")
    show()
}
```

### Java API

```java
import com.github.winterreisender.webviewko.*;

WebviewKo webview = new WebviewKo();
webview.title("webviewKo Java Test");
webview.size(1024,768,WindowHint.None);
webview.url("https://example.com");
webview.show();
```

### Native API

You can also use JNA bindings directly:

```kotlin
// This implemented the bind.c in webview
import com.github.winterreisender.webviewko.*

with(WebviewJNA.getInstance()) {
    val pWebview = webview_create(1, Pointer.NULL)
    webview_set_title(pWebview, "Hello")
    webview_set_size(pWebview, 800, 600, WebviewJNA.WEBVIEW_HINT_NONE)

    val html = """
        <button id="increment">Tap me</button>
        <div>You tapped <span id="count">0</span> time(s).</div>
        <script>
          ...
        </script>
    """.trimIndent()

    val callback = object : WebviewLibrary.webview_bind_fn_callback {
        override fun apply(seq: String?, req: String?, arg: Pointer?) {
            val r: Int = Regex("""\["(\d+)"]""").find(req!!)!!.groupValues[1].toInt() + 1
            webview_return(pWebview, seq, 0, "{count: $r}")
        }

    }
    webview_bind(pWebview, "increment", callback)
    webview_set_html(pWebview, html);
    webview_run(pWebview)
    webview_destroy(pWebview)
}
```

### CLI

```shell
java -jar webviewko.jar https://example.com
java -jar webviewko.jar https://example.com --title Hello --width 800 --height 600
```

## Contribution

All suggestions, pull requests, issues and other contributions are welcome and appreciated.

see [GitHub Discussions](https://github.com/Winterreisender/webviewko/discussions)

## Credits

| Project                                                       | License                                                                                          |
|---------------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| [wiverson/webviewjar](https://github.com/wiverson/webviewjar) | [MIT](https://github.com/wiverson/webviewjar/blob/master/LICENSE)                                |
| [webview_csharp](https://github.com/webview/webview_csharp)   | [MIT](https://github.com/webview/webview_csharp/blob/master/LICENSE)                             |
| [webview](https://github.com/webview/webview)                 | [MIT](https://github.com/webview/webview/blob/master/LICENSE)                                    |
| [JNA](https://github.com/java-native-access/jna)              | [LGPL-2.1-or-later OR Apache-2.0](https://github.com/java-native-access/jna/blob/master/LICENSE) |
| [Kotlin & kotlinx](https://kotlinlang.org/)                   | [Apache-2.0](https://github.com/JetBrains/kotlin/blob/master/LICENSE)                            |

## License

Copyright 2022 Winterreisender

Licensed under the Apache License, Version 2.0 (the "License");  
you may not use this file except in compliance with the License.  
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software  
distributed under the License is distributed on an "AS IS" BASIS,  
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
See the License for the specific language governing permissions and  
limitations under the License.

SPDX short identifier: **Apache-2.0**

![OSI Approved](https://opensource.org/files/OSIApproved_100X125.png)
