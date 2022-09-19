// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.github.winterreisender.webviewko.WebviewKo
import com.github.winterreisender.webviewko.WebviewKoCompose.Webview



@Composable
@Preview
fun App() {
    var uri by remember { mutableStateOf("http://info.cern.ch/hypertext/WWW/TheProject.html") }
    var initScript :String? by remember { mutableStateOf(null) }
    var debug by remember { mutableStateOf(true) }
    var bindings by remember { mutableStateOf( mapOf<String,WebviewKo.(String)->String>("sayHello" to { println("it"); ""}) ) }

    MaterialTheme {
        Column(Modifier.fillMaxSize()) {
            Webview(
                uri,
                debug = debug,
                modifier =  Modifier.fillMaxWidth().height(300.dp),
                init = initScript,
                bindings =  bindings //bindings.toMap()
            )

            Card(Modifier.padding(16.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Controller", fontSize = 1.7.em)
                    Divider()
                    Spacer(Modifier.padding(4.dp))
                    Row(Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = Arrangement.SpaceAround) {
                        var uriText by remember { mutableStateOf(uri) }
                        TextField(value = uriText, onValueChange = { uriText = it })
                        Button(onClick = {
                            uri = uriText
                        }) {
                            Text("Navigate")
                        }
                    }
                    Row(Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = Arrangement.SpaceAround) {
                        Checkbox(debug,{debug=it})
                        Text("Debug")
                    }
                    Row(Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = Arrangement.Start) {
                        var initText by remember { mutableStateOf(initScript) }
                        TextField(value = initText ?: "", onValueChange = { initText = it })
                        Button(onClick = {
                            initScript = initText
                        }) {
                            Text("Init")
                        }
                        Button({bindings = mapOf<String,WebviewKo.(String)->String>("sayHello2" to { println(it); ""}) }) {
                            Text("Change bindings")
                        }
                    }
                }
            }


        }

    }
}

class Test {

}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, state = rememberWindowState(size = DpSize(1024.dp,800.dp))) {
        App()
    }
}


