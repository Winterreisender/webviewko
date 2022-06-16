package io.github.winterreisender.webviewko
import kotlinx.cli.*

fun main(args :Array<String>) {
    with(ArgParser("webviewko")) {
        // parse args
        val uri by argument(ArgType.String, description = "URI/URL")
        val width by option(ArgType.Int, description = "window width in px").default(800)
        val height by option(ArgType.Int, description = "window height in px").default(600)
        val title  by option(ArgType.String, shortName = "t", description = "window title").default("webviewko")
        parse(args)

        //run webviewKo
        val webviewKo = WebviewKo(title,uri,width,height,WindowHint.None)
        webviewKo.show()
    }
}