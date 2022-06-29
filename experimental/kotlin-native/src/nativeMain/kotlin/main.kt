import kotlinx.cli.*
fun main(args: Array<String>) {
    with(ArgParser("webviewko")) {
        val url    by argument(ArgType.String, description = "URI/URL to navigate")
        val title  by option(ArgType.String, shortName = "t", description = "Window title").default("webviewko")
        val width  by option(ArgType.Int, description = "Window width in px").default(800)
        val height by option(ArgType.Int, description = "Window height in px").default(600)
        val hint   by option(ArgType.Choice<WebviewKo.WindowHint>(), description = "Window hint").default(WebviewKo.WindowHint.None)
        val init   by option(ArgType.String, description = "JS to run on page loading").default("")
        val debug  by option(ArgType.Boolean, description = "Debug mode").default(false)
        parse(args)

        try {
            WebviewKo(if(debug) 1 else 0).let {
                it.title(title)
                it.size(width, height, hint)
                if (init.isNotEmpty() ) {
                    it.init(init)
                }
                it.navigate(url)
                it.show()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}