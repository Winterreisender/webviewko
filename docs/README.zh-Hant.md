# webviewko

![Top language](https://img.shields.io/github/languages/top/Winterreisender/webviewko?color=b99bf8&logo=kotlin)
![Java](https://img.shields.io/badge/Java-11,17-orange?logo=java)
![license](https://img.shields.io/github/license/Winterreisender/webviewko)

![dev release](https://img.shields.io/github/v/release/Winterreisender/webviewko?label=dev&include_prereleases)
[![Gradle CI](https://github.com/Winterreisender/webviewko/actions/workflows/gradle-ci.yml/badge.svg)](https://github.com/Winterreisender/webviewko/actions/workflows/gradle-ci.yml)
![GitHub last commit](https://img.shields.io/github/last-commit/Winterreisender/webviewko)
[![JitPack](https://jitpack.io/v/Winterreisender/webviewko.svg)](https://jitpack.io/#Winterreisender/webviewko)

<!-- 
See [RFC4646](https://www.ietf.org/rfc/rfc4646.txt), [W3C language tags](https://www.w3.org/International/articles/language-tags/#bytheway) and [iana](https://www.iana.org/assignments/language-subtag-registry) for language tags
-->

[English](../README.md) | [中文(简体)](README.zh-Hans.md) | **中文(繁體)**

_此文件由中文(簡體)版本轉換而來_

webviewko 是一個 [webview](https://github.com/webview/webview) (輕量級跨平台的網頁視圖庫) 的 Kotlin/Java 綁定.

![screenshot](../screenshot.jpg)

## 快速開始

### 1. 導入 webviewko

如果你在用 Gradle 或者 Maven 等構建系統, 請看 [JitPack.io上的webviewko](https://jitpack.io/#Winterreisender/webviewko)

如果你想手動導入jar文件, 請到 [GitHub Release](https://github.com/Winterreisender/webviewko/releases) 下載.

### 2. 使用 webviewko

對於 Kotlin:

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

對於 Java:

```java
import com.github.winterreisender.webviewko.*;

WebviewKo webview = new WebviewKo();
webview.title("webviewKo Java Test");
webview.size(1024,768,WindowHint.None);
webview.url("https://example.com");

webview.show();
```

#### 原生API

你也可以直接使用 webviewko 的 JNA 綁定:

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

更多例子,諸如綁定JS回調、在線程間共享實例: 請看 [TestKt.kt](https://github.com/Winterreisender/webviewko/blob/main/src/test/java/TestKt.kt) 和 [TestJava.java](https://github.com/Winterreisender/webviewko/blob/main/src/test/java/TestJava.java)


## 文檔

**請看 [docs](https://winterreisender.github.io/webviewko/)**

## 貢獻者指南

我們歡迎並感謝任何人對項目的任何貢獻，包括建議、Pull Request、Issue等。

## 引用

| 項目                                                                           | 許可證                                                                                              |
|------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| [wiverson/webviewjar](https://github.com/wiverson/webviewjar)                | [MIT](https://github.com/wiverson/webviewjar/blob/master/LICENSE)                                |
| [webview_csharp](https://github.com/webview/webview_csharp)                  | [MIT](https://github.com/webview/webview_csharp/blob/master/LICENSE)                             |
| [webview](https://github.com/webview/webview)                                | [MIT](https://github.com/webview/webview/blob/master/LICENSE)                                    |
| [JNA](https://github.com/java-native-access/jna)                             | [LGPL-2.1-or-later OR Apache-2.0](https://github.com/java-native-access/jna/blob/master/LICENSE) |
| [Microsoft Webview2](https://www.nuget.org/packages/Microsoft.Web.WebView2/) | [See the License](https://www.nuget.org/packages/Microsoft.Web.WebView2/1.0.1245.22/License)     |
| [Kotlin & kotlinx](https://kotlinlang.org/)                                  | [Apache-2.0](https://github.com/JetBrains/kotlin/blob/master/LICENSE)                            |

## 版權與許可

Copyright 2022 Winterreisender

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0  
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
See the License for the specific language governing permissions and limitations under the License.

SPDX short identifier: **Apache-2.0**

![OSI Approved](https://opensource.org/files/OSIApproved_100X125.png)
# webviewko

![Top language](https://img.shields.io/github/languages/top/Winterreisender/webviewko?color=b99bf8&logo=kotlin)
![Java](https://img.shields.io/badge/Java-11,17-orange?logo=java)
![license](https://img.shields.io/github/license/Winterreisender/webviewko)

![dev release](https://img.shields.io/github/v/release/Winterreisender/webviewko?label=dev&include_prereleases)
[![Gradle CI MacOS](https://github.com/Winterreisender/webviewko/actions/workflows/gradle-ci.yml/badge.svg)](https://github.com/Winterreisender/webviewko/actions/workflows/gradle-ci.yml)
![GitHub last commit](https://img.shields.io/github/last-commit/Winterreisender/webviewko)
[![JitPack](https://jitpack.io/v/Winterreisender/webviewko.svg)](https://jitpack.io/#Winterreisender/webviewko)

<!-- 
See [RFC4646](https://www.ietf.org/rfc/rfc4646.txt), [W3C language tags](https://www.w3.org/International/articles/language-tags/#bytheway) and [iana](https://www.iana.org/assignments/language-subtag-registry) for language tags
-->

[English](../README.md) | **中文(簡體)** | [中文(繁體)](README.zh-Hant.md)


webviewko 是一個 [webview](https://github.com/webview/webview) (輕量級跨平台的網頁視圖庫) 的 Kotlin 綁定.

![screenshot](../screenshot.jpg)

## 快速開始

### 1. 導入 webviewko

如果你在用 Gradle 或者 Maven 等構建系統, 請看 [JitPack.io上的webviewko](https://jitpack.io/#Winterreisender/webviewko)

如果你想手動導入jar文件, 請到 [GitHub Release](https://github.com/Winterreisender/webviewko/releases) 下載.

### 2. 使用 webviewko

對於 Kotlin:

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

對於 Java:

```java
import com.github.winterreisender.webviewko.*;

WebviewKo webview = new WebviewKo();
webview.title("webviewKo Java Test");
webview.size(1024,768,WindowHint.None);
webview.url("https://example.com");

webview.show();
```

#### 原生API

你也可以直接使用 webviewko 的 JNA 綁定:

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

更多例子,諸如綁定JS回調、在線程間共享實例: 請看 [TestKt.kt](https://github.com/Winterreisender/webviewko/blob/main/src/test/java/TestKt.kt) 和 [TestJava.java](https://github.com/Winterreisender/webviewko/blob/main/src/test/java/TestJava.java)


## 文檔

**請看 [docs](https://winterreisender.github.io/webviewko/)**

## 貢獻者指南

我們歡迎並感謝任何人對項目的任何貢獻，包括建議、Pull Request、Issue等。

## 引用

| 項目                                                                           | 許可證                                                                                              |
|------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| [wiverson/webviewjar](https://github.com/wiverson/webviewjar)                | [MIT](https://github.com/wiverson/webviewjar/blob/master/LICENSE)                                |
| [webview_csharp](https://github.com/webview/webview_csharp)                  | [MIT](https://github.com/webview/webview_csharp/blob/master/LICENSE)                             |
| [webview](https://github.com/webview/webview)                                | [MIT](https://github.com/webview/webview/blob/master/LICENSE)                                    |
| [JNA](https://github.com/java-native-access/jna)                             | [LGPL-2.1-or-later OR Apache-2.0](https://github.com/java-native-access/jna/blob/master/LICENSE) |
| [Microsoft Webview2](https://www.nuget.org/packages/Microsoft.Web.WebView2/) | [See the License](https://www.nuget.org/packages/Microsoft.Web.WebView2/1.0.1245.22/License)     |
| [Kotlin & kotlinx](https://kotlinlang.org/)                                  | [Apache-2.0](https://github.com/JetBrains/kotlin/blob/master/LICENSE)                            |

## 版權與許可

Copyright 2022 Winterreisender

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0  
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
See the License for the specific language governing permissions and limitations under the License.

SPDX short identifier: **Apache-2.0**

![OSI Approved](https://opensource.org/files/OSIApproved_100X125.png)
