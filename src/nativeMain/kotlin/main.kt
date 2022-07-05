/*
 * Copyright (C) 2022. Winterreisender
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX short identifier: Apache-2.0
 */

/**
 * The Commandline interface for Kotlin/Native
 */

import kotlinx.cli.*
import com.github.winterreisender.webviewko.WebviewKo


fun main(args: Array<String>)
{
    with(ArgParser("webviewko")) {
        val url    by argument(ArgType.String, description = "URI/URL to navigate").optional()
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
                it.navigate(url ?: "https://github.com/Winterreisender/webviewkoCLI/wiki/Webviewko-CLI-Help")
                it.show()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

}