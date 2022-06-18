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

package com.github.winterreisender.webviewko
import kotlinx.cli.*

fun main(args :Array<String>) {
    //System.setProperty("jna.debug_load","true")
    //System.setProperty("jna.tmpdir",".")

    with(ArgParser("webviewko")) {
        // parse args
        val url by argument(ArgType.String, description = "URI/URL")
        val width by option(ArgType.Int, description = "window width in px").default(800)
        val height by option(ArgType.Int, description = "window height in px").default(600)
        val title  by option(ArgType.String, shortName = "t", description = "window title").default("webviewko")
        parse(args)

        //run webviewKo
        try {
            with(WebviewKo()) {
                title(title)
                size(width, height)

                url(url)
                show()
            }
        } catch (e :Throwable) {
            e.printStackTrace()
        }

    }
}