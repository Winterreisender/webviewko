# webviewko

![Kotlin](https://img.shields.io/badge/Kotlin%2FJVM-7F52FF?logo=kotlin&logoColor=FFFFFF)
![Kotlin](https://img.shields.io/badge/Kotlin%2FNative-7F52FF?logo=kotlin&logoColor=FFFFFF)
![license](https://img.shields.io/github/license/Winterreisender/webviewko?color=3DA639)
![release](https://img.shields.io/github/v/release/Winterreisender/webviewko?label=release&include_prereleases)
[![gradle ci](https://github.com/Winterreisender/webviewko/actions/workflows/gradle-ci.yml/badge.svg)](https://github.com/Winterreisender/webviewko/actions/workflows/gradle-ci.yml)
![last commit](https://img.shields.io/github/last-commit/Winterreisender/webviewko)

[English](../README.md) | **中文(简体)** | [中文(繁體)](README.zh-Hant.md) 


webviewko 是轻量跨平台的网页视图库 [webview](https://github.com/webview/webview) 的 Kotlin 绑定, 支持 Java 和 Kotlin/Native.

![screenshot](../screenshot.jpg)

## 快速开始

### 1. 导入 webviewko

如果你在用 Gradle 或者 Maven 等构建系统,我们建议您通过 GitLab Packages 来使用 webviewko.

对于`build.gradle.kts`, 使用:

```kotlin {3}
repositories {
    mavenCentral()
    maven("https://gitlab.com/api/v4/projects/38224197/packages/maven") // 添加GitLab Packages作为Maven仓库
}

implementation("com.github.winterreisender:webviewko:0.4.0")            // Kotlin Multiplatform
implementation("com.github.winterreisender:webviewko-jvm:0.4.0")        // Java 和 Kotlin/JVM
implementation("com.github.winterreisender:webviewko-linuxx64:0.4.0")   // Kotlin/Native Linux
implementation("com.github.winterreisender:webviewko-mingwx64:0.4.0")   // Kotlin/Native Windows
```
详情请查看 [GitLab Packages 上的 webviewko](https://gitlab.com/Winterreisender/webviewko/-/packages/)

如果需要手动导入jar文件, 请到 [GitHub Release](https://github.com/Winterreisender/webviewko/releases) 下载。

### 2. 使用 webviewko

对于 Kotlin/JVM 和 Kotlin/Native:

```kotlin
import com.github.winterreisender.webviewko.WebviewKo

WebviewKo().run {
    title("Title")
    size(800, 600)
    url("https://example.com")
    show()
}
```

对于 Java:

```java
import com.github.winterreisender.webviewko.WebviewKo;

WebviewKo webview = new WebviewKo(0);
webview.title("Test");
webview.size(1024,768,WebviewKo.WindowHint.None);
webview.url("https://example.com");
webview.show();
```

### 3.与 webview 交互

可以用 `bind`,`init`,`dispatch` 和 `eval` 来和 webview 进行交互。

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


## 文档

- [API 参考 (KDoc)](https://winterreisender.github.io/webviewko/docs/kdoc/index.html)
- [GitHub Wiki](https://github.com/Winterreisender/webviewko/wiki)
- 示例
    - [Test](https://github.com/Winterreisender/webviewko/blob/main/src/commonTest/kotlin/Test.kt) (Kotlin/Multiplatform)
    - [TestKt](https://github.com/Winterreisender/webviewko/blob/main/src/jvmTest/kotlin/TestKt.kt) (Kotlin/JVM)
    - [TestJava](https://github.com/Winterreisender/webviewko/blob/main/src/jvmTest/java/TestJava.java) (Java)
    - [TestNative](https://github.com/Winterreisender/webviewko/blob/main/src/nativeTest/kotlin/TestNative.kt) (Kotlin/Native)
- [webview 的文档](https://webview.dev/)

## 演示

命令行界面: [Winterreisender/webviewkoCLI](https://github.com/Winterreisender/webviewkoCLI)

## 贡献

欢迎并感谢所有Issue, Pull Request和其他形式的贡献。

## 引用

| 项目                                                                           | 许可                                                                                               |
|------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| [wiverson/webviewjar](https://github.com/wiverson/webviewjar)                | [MIT](https://github.com/wiverson/webviewjar/blob/master/LICENSE)                                |
| [webview_csharp](https://github.com/webview/webview_csharp)                  | [MIT](https://github.com/webview/webview_csharp/blob/master/LICENSE)                             |
| [webview](https://github.com/webview/webview)                                | [MIT](https://github.com/webview/webview/blob/master/LICENSE)                                    |
| [JNA](https://github.com/java-native-access/jna)                             | [LGPL-2.1-or-later OR Apache-2.0](https://github.com/java-native-access/jna/blob/master/LICENSE) |
| [Microsoft Webview2](https://www.nuget.org/packages/Microsoft.Web.WebView2/) | [See the License](https://www.nuget.org/packages/Microsoft.Web.WebView2/1.0.1245.22/License)     |
| [Kotlin & kotlinx](https://kotlinlang.org/)                                  | [Apache-2.0](https://github.com/JetBrains/kotlin/blob/master/LICENSE)                            |

## 版权与许可

Copyright 2022 Winterreisender and [other contributors](https://github.com/Winterreisender/webviewko/graphs/contributors).

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0  
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
See the License for the specific language governing permissions and limitations under the License.

SPDX 许可证标识符: **Apache-2.0**

![OSI Approved](https://opensource.org/files/OSIApproved_100X125.png)
