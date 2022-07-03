# webviewko

![Kotlin](https://img.shields.io/badge/Kotlin%2FJVM-7F52FF?logo=kotlin&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin%2FNative-7F52FF?logo=kotlin&logoColor=white)
![license](https://img.shields.io/github/license/Winterreisender/webviewko) 

![release](https://img.shields.io/github/v/release/Winterreisender/webviewko?label=release&include_prereleases)
[![Gradle CI](https://github.com/Winterreisender/webviewko/actions/workflows/gradle-ci.yml/badge.svg)](https://github.com/Winterreisender/webviewko/actions/workflows/gradle-ci.yml)
![GitHub last commit](https://img.shields.io/github/last-commit/Winterreisender/webviewko)
[![JitPack](https://jitpack.io/v/Winterreisender/webviewko.svg)](https://jitpack.io/#Winterreisender/webviewko)

<!-- 
See [RFC4646](https://www.ietf.org/rfc/rfc4646.txt), [W3C language tags](https://www.w3.org/International/articles/language-tags/#bytheway) and [iana](https://www.iana.org/assignments/language-subtag-registry)
-->

**English** | [中文(简体)](docs/README.zh-Hans.md) | [中文(繁體)](docs/README.zh-Hant.md) 

webviewko provides a Kotlin/JVM and a Kotlin/Native(experimental) binding to [webview](https://github.com/webview/webview), a tiny cross-platform webview library to build modern cross-platform GUIs using [WebView2](https://developer.microsoft.com/en-us/microsoft-edge/webview2/), WebKit and [WebKitGTK](https://webkitgtk.org/).

![screenshot](screenshot.jpg)

## Getting Started

### 1. Import webviewko

If you're using a build system like Gradle or Maven, see [webviewko in JitPack.io](https://jitpack.io/#Winterreisender/webviewko)

If you want to use jar files, see [GitHub Release](https://github.com/Winterreisender/webviewko/releases)

### 2. Use webviewko

For Kotlin/JVM and Kotlin/Native:

```kotlin
import com.github.winterreisender.webviewko.*

with(WebviewKo()) {
    title("Basic Example")
    size(480, 320, WindowHint.None)
    html("Thanks for using webview!")
    show()
}
```

For Java:

```java
import com.github.winterreisender.webviewko.*;

WebviewKo webview = new WebviewKo();
webview.title("webviewKo Java Test");
webview.size(1024,768,WindowHint.None);
webview.url("https://example.com");

webview.show();
```

#### Native API using JNA

You can also use JNA bindings directly:

```kotlin
import com.github.winterreisender.webviewko.*
import com.sun.jna.Pointer

with(WebviewJNA.getLib()) {
    val pWebview = webview_create(1, Pointer.NULL)
    webview_navigate(pWebview, "https://example.com")
    webview_run(pWebview)
    webview_destroy(pWebview)
}
```

or in Java:

```java
WebviewJNA.WebviewLibrary lib = WebviewJNA.Companion.getLib();
Pointer pWebview = lib.webview_create(1, Pointer.NULL);
lib.webview_navigate(pWebview, "https://example.com");
lib.webview_run(pWebview);
lib.webview_destroy(pWebview);
```

More examples like binding a Kotlin/Java callback or running in a thread: see [TestKt.kt](https://github.com/Winterreisender/webviewko/blob/main/src/jvmTest/kotlin/TestKt.kt) and [TestJava.java](https://github.com/Winterreisender/webviewko/blob/main/src/jvmTest/java/TestJava.java)

### Kotlin/Native Bindings

webviewko also provides a Kotlin/Native binding for Windows and Linux with the same API as JVM.

You can also use the C API directly:

```kotlin
val w : webview_t = webview_create(0,null)!!
webview_set_size(w, 800, 600, 0)
webview_navigate(w, "https://example.com")
webview_run(w)
webview_destroy(w)
```


## Documentation

**See [docs](https://winterreisender.github.io/webviewko/) for the full document**

## Contribution

All suggestions, pull requests, issues and other contributions are welcome and appreciated.

## Credits

| Project                                                                      | License                                                                                          |
|------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| [wiverson/webviewjar](https://github.com/wiverson/webviewjar)                | [MIT](https://github.com/wiverson/webviewjar/blob/master/LICENSE)                                |
| [webview_csharp](https://github.com/webview/webview_csharp)                  | [MIT](https://github.com/webview/webview_csharp/blob/master/LICENSE)                             |
| [webview](https://github.com/webview/webview)                                | [MIT](https://github.com/webview/webview/blob/master/LICENSE)                                    |
| [JNA](https://github.com/java-native-access/jna)                             | [LGPL-2.1-or-later OR Apache-2.0](https://github.com/java-native-access/jna/blob/master/LICENSE) |
| [Microsoft Webview2](https://www.nuget.org/packages/Microsoft.Web.WebView2/) | [See the License](https://www.nuget.org/packages/Microsoft.Web.WebView2/1.0.1245.22/License)     |
| [Kotlin & kotlinx](https://kotlinlang.org/)                                  | [Apache-2.0](https://github.com/JetBrains/kotlin/blob/master/LICENSE)                            |

## License

Copyright 2022 Winterreisender

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0  
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
See the License for the specific language governing permissions and limitations under the License.

SPDX short identifier: **Apache-2.0**

![OSI Approved](https://opensource.org/files/OSIApproved_100X125.png)
