package com.github.winterreisender.webviewko

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel

object WebviewKoCompose {
    // TODO: HTML support
    @Composable
    fun Webview(
        url :String,
        bindings :Map<String, WebviewKo.(String)->String> = mapOf(),
        init :String? = null,
        debug :Boolean = false,
        modifier : Modifier = Modifier
    ) {
        val webview = remember(debug) {
            WebviewKoAWT(if (debug) 1 else 0) {
                init?.let(it::init)
                bindings.forEach { (k, v) -> it.bind(k, v) }
                it.navigate(url)
                it.show()
            }
        }

        LaunchedEffect(url) {
            if(webview.isInitialized)
                webview.dispatch {
                    navigate(url)
                }
        }

        DisposableEffect(bindings) {
            println(bindings)
            if(webview.isInitialized)
                webview.dispatch {
                    bindings.forEach { (k, v) -> webview.dispatch { bind(k,v) }}
                }
            onDispose {
                println("onDispose $bindings")
                if(webview.isInitialized)
                    webview.dispatch {
                        bindings.forEach { (k, v) -> webview.dispatch { unbind(k) }}
                    }
            }
        }

        LaunchedEffect(init) {
            if(webview.isInitialized)
                webview.dispatch {
                    init?.let { init(it) }
                }
        }

        SwingPanel(
            modifier=modifier,
            factory = remember(debug) {{
                webview
            }}
        )
    }

}