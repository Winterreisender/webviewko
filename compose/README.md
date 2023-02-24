**These features are experimental and only tested in Windows by now.**


### Integrate with Swing, AWT and Jetpack Compose

<details>
  <summary>Screenshot</summary>
  <img src="compose/screenshot-compose.jpg"/>
</details>

For Swing or AWT

```kotlin
import com.github.winterreisender.webviewko.WebviewKoAWT
// Since 0.6.0
fun main() {
    JFrame().apply {
        size = Dimension(800,800)
        JPanel().apply {
            val webview = WebviewKoAWT(1) {
                it.navigate("https://example.com")
                it.show()
            }.also { add(it,BorderLayout.CENTER) }
            JButton("Change URL").apply {
                addActionListener {
                    webview.dispatch {navigate("https://neverssl.com")}
                }
            }.also { add(it,BorderLayout.SOUTH) }
        }.also { add(it) }
        isVisible = true
    }
}
```

For Jetpack Compose Desktop

```kotlin
// Import: implementation("com.github.winterreisender:webviewko-compose:0.6.0-SNAPSHOT")
import com.github.winterreisender.webviewko.WebviewKoCompose.Webview
fun main() = singleWindowApplication {
    Webview(
      url = "https://example.com",
      modifier = Modifier.fillMaxSize()
    )
}
```

Interact with webview in Jetpack Compose Desktop:

```kotlin
fun main() = singleWindowApplication {
        var uri by remember { mutableStateOf("http://info.cern.ch/hypertext/WWW/TheProject.html") }
        var initScript :String? by remember { mutableStateOf(null) }
        var debug by remember { mutableStateOf(true) }
        var bindings by remember { mutableStateOf( mapOf<String,WebviewKo.(String)->String>("sayHello" to { println("it"); ""}) ) }
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Webview(
                uri,
                debug = debug,
                init = initScript,
                bindings =  bindings,
                modifier =  Modifier.fillMaxWidth().height(400.dp)
            )
            Column(Modifier.padding(16.dp).fillMaxWidth()) {
                Row {
                    var uriText by remember { mutableStateOf(uri) }
                    OutlinedTextField(value = uriText, onValueChange = { uriText = it })
                    Button(onClick = {
                        uri = uriText
                    }) { Text("Navigate") }
                }
                Checkbox(debug, { debug = it })
                Button({ bindings = mapOf("sayHello2" to { println(it); "" }) }) {Text("Change bindings")}
                Row {
                    var initText by remember { mutableStateOf(initScript) }
                    OutlinedTextField(value = initText ?: "", onValueChange = { initText = it })
                    Button(onClick = {initScript = initText }) {Text("Init")}
                }}}}
```