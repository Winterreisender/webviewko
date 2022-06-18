/*
 * Copyright (c) 2022  Winterreisender
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX short identifier: **Apache-2.0**
 */

import com.github.winterreisender.webviewko.*;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class TestJava {
    @Test
    void apiSimple() {
        if (!Desktop.isDesktopSupported())
            return;

        WebviewKo webview = new WebviewKo();
        webview.title("webviewKo Java Test");
        webview.size(1024,768,WindowHint.None);
        webview.url("https://example.com");
        webview.show();
    }

    @Test
    void apiFull() {
        // TODO
    }

    @Test
    void jnaFull() {
        // TODO
    }
}
