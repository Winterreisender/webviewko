package com.github.winterreisender.webviewko
import kotlinx.cli.*

fun main(args :Array<String>) {
    //System.setProperty("jna.debug_load","true")
    //System.setProperty("jna.tmpdir",".")

    with(ArgParser("webviewko")) {
        // parse args
        val uri by argument(ArgType.String, description = "URI/URL")
        val width by option(ArgType.Int, description = "window width in px").default(800)
        val height by option(ArgType.Int, description = "window height in px").default(600)
        val title  by option(ArgType.String, shortName = "t", description = "window title").default("webviewko")
        parse(args)

        //run webviewKo
        try {
            val webviewKo = WebviewKo(title, uri, width, height, WindowHint.None)
            webviewKo.show()
        } catch (e :Throwable) {
            e.printStackTrace()
        }

    }
}