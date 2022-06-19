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
import com.sun.jna.Pointer;
import org.jetbrains.annotations.Nullable;
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
        if (!Desktop.isDesktopSupported())
            return;

        WebviewKo webview = new WebviewKo();
        webview.title("webviewKo Java Test");
        webview.size(1024,768,WindowHint.None);

        webview.bind("increment",(WebviewKo w,String msg)-> {
            System.out.println(msg);
            w.title(msg);
            return "{count: 7}";
        });

        webview.html("""
                <button id="increment">Tap me</button>
                <div>You tapped <span id="count">0</span> time(s).</div>
                <script>
                  const [incrementElement, countElement] = document.querySelectorAll("#increment, #count");
                  document.addEventListener("DOMContentLoaded", () => {
                    incrementElement.addEventListener("click", () => {
                      window.increment(countElement.innerText).then(result => {
                        countElement.textContent = result.count;
                      });
                    });
                  });
                </script>
            """.trim());

        webview.show();
    }


    // Callback for jnaFull
    static class Callback0 implements WebviewJNA.WebviewLibrary.webview_bind_fn_callback {
        private WebviewJNA.WebviewLibrary lib;
        private Pointer pWebview;

        public Callback0(WebviewJNA.WebviewLibrary lib, Pointer pWebview){
            this.lib = lib;
            this.pWebview = pWebview;
        }

        @Override
        public void apply(@Nullable String seq, @Nullable String req, @Nullable Pointer arg) {
            System.out.println("seq=" + seq);
            System.out.println("req=" + req);
            lib.webview_set_title(pWebview,"Hello from Java");
            lib.webview_return(pWebview, seq,0,"{count: 7}");
        }
    }

    @Test
    void jnaFull() {
        var lib = WebviewJNA.Companion.getLib();

        // This test implemented the bind.c in webview

        if (!Desktop.isDesktopSupported()) {
            return;
        }
        var pWebview = lib.webview_create(1, Pointer.NULL);
        lib.webview_set_title(pWebview, "Hello");
        lib.webview_set_size(pWebview, 800, 600, WebviewJNA.WEBVIEW_HINT_NONE);

        lib.webview_init(pWebview, """
                console.log("Hello, from init")
        """);

        var html = """
            <button id="increment">Tap me</button>
            <div>You tapped <span id="count">0</span> time(s).</div>
            <script>
              const [incrementElement, countElement] = document.querySelectorAll("#increment, #count");
              document.addEventListener("DOMContentLoaded", () => {
                incrementElement.addEventListener("click", () => {
                  window.increment(countElement.innerText).then(result => {
                    countElement.textContent = result.count;
                  });
                });
              });
            </script>
        """.trim();


        lib.webview_bind(pWebview,"increment", new Callback0(lib,pWebview), Pointer.NULL);
        lib.webview_set_html(pWebview, html);

        lib.webview_eval(pWebview, """
                console.log("Hello, from  eval")
        """);

        lib.webview_run(pWebview);
        lib.webview_destroy(pWebview);
    }
}
