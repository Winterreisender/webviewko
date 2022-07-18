# webviewko

![Kotlin](https://img.shields.io/badge/Kotlin%2FJVM-7F52FF?logo=kotlin&logoColor=FFFFFF)
![Kotlin](https://img.shields.io/badge/Kotlin%2FNative-7F52FF?logo=kotlin&logoColor=FFFFFF)
![license](https://img.shields.io/github/license/Winterreisender/webviewko?color=3DA639) 

![release](https://img.shields.io/github/v/release/Winterreisender/webviewko?label=release&include_prereleases)
[![gradle ci](https://github.com/Winterreisender/webviewko/actions/workflows/gradle-ci.yml/badge.svg)](https://github.com/Winterreisender/webviewko/actions/workflows/gradle-ci.yml)
![last commit](https://img.shields.io/github/last-commit/Winterreisender/webviewko)

<!-- 
See [RFC4646](https://www.ietf.org/rfc/rfc4646.txt), [W3C language tags](https://www.w3.org/International/articles/language-tags/#bytheway) and [iana](https://www.iana.org/assignments/language-subtag-registry)
-->

**English** | [中文(简体)](docs/README.zh-Hans.md) | [中文(繁體)](docs/README.zh-Hant.md) 

webviewko provides a Kotlin/JVM and a Kotlin/Native binding to [webview](https://github.com/webview/webview), a tiny cross-platform webview library to build modern cross-platform desktop GUIs using [WebView2](https://developer.microsoft.com/en-us/microsoft-edge/webview2/), WebKit and [WebKitGTK](https://webkitgtk.org/).

![screenshot](screenshot.jpg)

<!--
## Highlights

### Kotlin/JVM and Java

- Tiny size: The demo jar distribution is `<5MB`
- Support Windows, Linux and macOS

### Kotlin/Native

- Tiny size: The demo is `<1MB` for Linux and `<1.5MB` for Windows
- Support Windows and Linux
-->

## Getting Started

### 1. Import webviewko

If you're using a build system like Gradle or Maven, you may have a look at [GitHub Packages](https://github.com/Winterreisender?tab=packages&repo_name=webviewko)

If you want to use jar files, see [GitHub Release](https://github.com/Winterreisender/webviewko/releases)

Other helpful resources: 
- [Use webviewko GitHub Packages for Kotlin/JVM and Java](https://github.com/Winterreisender/webviewko/wiki/How-to-use#use-github-packages-for-kotlinjvm-and-java)
- [Use webviewko GitHub Packages for Kotlin/Native](https://github.com/Winterreisender/webviewko/wiki/How-to-use#use-github-packages-for-kotlinnative)

### 2. Use webviewko

For Kotlin/JVM and Kotlin/Native:

```kotlin
import com.github.winterreisender.webviewko.WebviewKo

WebviewKo().run {
    title("Title")
    size(800, 600)
    url("https://example.com")
    show()
}
```

For Java:

```java
import com.github.winterreisender.webviewko.WebviewKo;

WebviewKo webview = new WebviewKo(0);
webview.title("Test");
webview.size(1024,768,WebviewKo.WindowHint.None);
webview.url("https://example.com");
webview.show();
```

### 3. Interact with webview

You can use `bind`,`init`,`dispatch` and `eval` to interact with your webview:

```kotlin
import com.github.winterreisender.webviewko.WebviewKo

WebviewKo().run {
    title("Test")
    init("""console.log("Hello, from  init")""")
    bind("increment") {
        val r :Int = Regex("""\["(\d+)"]""").find(it!!)!!.groupValues[1].toInt() + 1
        println(r.toString())
        if(r==8) terminate()
        "{count: $r}"
    }
  
    html("""<button id="increment">Tap me</button>
        <div>You tapped <span id="count">0</span> time(s).</div>
        <script>const [incrementElement, countElement] = document.querySelectorAll("#increment, #count");
          document.addEventListener("DOMContentLoaded", () => {
            incrementElement.addEventListener("click", () => {
              window.increment(countElement.innerText).then(result => {
                countElement.textContent = result.count;
              });});});
         </script>""")
    show()
}
```

<!-- You can also use JNA and Kotlin/Native bindings directly -->


## Documentation

- [API Reference (KDoc)](https://winterreisender.github.io/webviewko/docs/kdoc/index.html)
- [GitHub Wiki](https://github.com/Winterreisender/webviewko/wiki)
- Examples
  - [Test](https://github.com/Winterreisender/webviewko/blob/main/src/commonTest/kotlin/Test.kt) (Kotlin/Multiplatform)
  - [TestKt](https://github.com/Winterreisender/webviewko/blob/main/src/jvmTest/kotlin/TestKt.kt) (Kotlin/JVM)
  - [TestJava](https://github.com/Winterreisender/webviewko/blob/main/src/jvmTest/java/TestJava.java) (Java)
  - [TestNative](https://github.com/Winterreisender/webviewko/blob/main/src/nativeTest/kotlin/TestNative.kt) (Kotlin/Native)
- [webview Documentation](https://webview.dev/)

## Demos

A commandline interface for JVM and Native: [Winterreisender/webviewkoCLI](https://github.com/Winterreisender/webviewkoCLI)

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

Copyright 2022 Winterreisender and [other contributors](https://github.com/Winterreisender/webviewko/graphs/contributors).

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0  
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
See the License for the specific language governing permissions and limitations under the License.

SPDX short identifier: **Apache-2.0**

<img src="https://opensource.org/sites/default/files/public/OSIApproved.svg" width="100" />
