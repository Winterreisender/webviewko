# webviewko

![Kotlin](https://img.shields.io/badge/Kotlin%2FJVM-b69bef?logo=kotlin&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin%2FNative(experimental)-b69bef?logo=kotlin&logoColor=white)
![os](https://img.shields.io/badge/os-windows%20%7C%20linux%20%7C%20macos-blue)
![license](https://img.shields.io/github/license/Winterreisender/webviewko)

![release](https://img.shields.io/github/v/release/Winterreisender/webviewko?label=release&include_prereleases)
[![Gradle CI](https://github.com/Winterreisender/webviewko/actions/workflows/gradle-ci.yml/badge.svg)](https://github.com/Winterreisender/webviewko/actions/workflows/gradle-ci.yml)
![GitHub last commit](https://img.shields.io/github/last-commit/Winterreisender/webviewko)
[![JitPack](https://jitpack.io/v/Winterreisender/webviewko.svg)](https://jitpack.io/#Winterreisender/webviewko)

[English](../README.md) | **中文(简体)** | [中文(繁體)](README.zh-Hant.md) 


webviewko 是一个 [webview](https://github.com/webview/webview) (轻量级跨平台的网页视图库) 的 Kotlin/Java 绑定.

![screenshot](../screenshot.jpg)

## 快速开始

### 1. 导入 webviewko

如果你在用 Gradle 或者 Maven 等构建系统, 请看 [JitPack.io上的webviewko](https://jitpack.io/#Winterreisender/webviewko)

如果你想手动导入jar文件, 请到 [GitHub Release](https://github.com/Winterreisender/webviewko/releases) 下载.

### 2. 使用 webviewko

对于 Kotlin:

```kotlin
import com.github.winterreisender.webviewko.*
import com.sun.jna.Pointer;

with(WebviewKo()) {
    title("Basic Example")
    size(480, 320, WindowHint.None)
    html("Thanks for using webview!")
    show()
}
```

对于 Java:

```java
import com.github.winterreisender.webviewko.*;

WebviewKo webview = new WebviewKo();
webview.title("webviewKo Java Test");
webview.size(1024,768,WindowHint.None);
webview.url("https://example.com");

webview.show();
```

#### 原生API

你也可以直接使用 webviewko 的 JNA 绑定:

```kotlin
import com.github.winterreisender.webviewko.*
import com.github.winterreisender.webviewko.WebviewJNA.WebviewLibrary
import com.sun.jna.Pointer
import java.beans.JavaBean

with(WebviewJNA.getLib()) {
    val pWebview = webview_create(1, Pointer.NULL)
    webview_set_title(pWebview, "Hello")
    webview_set_size(pWebview, 800, 600, WebviewJNA.WEBVIEW_HINT_NONE)
    webview_navigate(pWebview, "https://example.com")
    webview_run(pWebview)
    webview_destroy(pWebview)
}
```

或者用Java:

```java
WebviewJNA.WebviewLibrary lib = WebviewJNA.Companion.getLib();
Pointer pWebview = lib.webview_create(1, Pointer.NULL);
lib.webview_set_title(pWebview, "Hello");
lib.webview_set_size(pWebview, 800, 600, WebviewJNA.WEBVIEW_HINT_NONE);
lib.webview_navigate(pWebview, "https://example.com");
lib.webview_run(pWebview);
lib.webview_destroy(pWebview);
```

更多例子,诸如绑定JS回调、在线程间共享实例: 请看 [TestKt.kt](https://github.com/Winterreisender/webviewko/blob/main/src/test/kotlin/TestKt.kt) 和 [TestJava.java](https://github.com/Winterreisender/webviewko/blob/main/src/test/java/TestJava.java)


## 文档

**请看 [docs](https://winterreisender.github.io/webviewko/)**

## 贡献者指南

我们欢迎并感谢任何人对项目的任何贡献，包括建议、Pull Request、Issue等。

## 引用

| 项目                                                                           | 许可证                                                                                              |
|------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| [wiverson/webviewjar](https://github.com/wiverson/webviewjar)                | [MIT](https://github.com/wiverson/webviewjar/blob/master/LICENSE)                                |
| [webview_csharp](https://github.com/webview/webview_csharp)                  | [MIT](https://github.com/webview/webview_csharp/blob/master/LICENSE)                             |
| [webview](https://github.com/webview/webview)                                | [MIT](https://github.com/webview/webview/blob/master/LICENSE)                                    |
| [JNA](https://github.com/java-native-access/jna)                             | [LGPL-2.1-or-later OR Apache-2.0](https://github.com/java-native-access/jna/blob/master/LICENSE) |
| [Microsoft Webview2](https://www.nuget.org/packages/Microsoft.Web.WebView2/) | [See the License](https://www.nuget.org/packages/Microsoft.Web.WebView2/1.0.1245.22/License)     |
| [Kotlin & kotlinx](https://kotlinlang.org/)                                  | [Apache-2.0](https://github.com/JetBrains/kotlin/blob/master/LICENSE)                            |

## 版权与许可

Copyright 2022 Winterreisender

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0  
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
See the License for the specific language governing permissions and limitations under the License.

SPDX short identifier: **Apache-2.0**

![OSI Approved](https://opensource.org/files/OSIApproved_100X125.png)
