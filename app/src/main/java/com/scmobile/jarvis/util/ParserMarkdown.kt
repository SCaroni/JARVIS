package com.scmobile.jarvis.util

object ParserMarkdown {

    fun extract(input: String): String {

        val start = "JARVIS SAVE"
        val end = "JARVIS SAVE"

        val startIndex = input.indexOf(start)

        if (startIndex == -1) return input

        val endIndex = input.lastIndexOf(end)

        if (endIndex <= startIndex) return input

        return input
            .substring(startIndex + start.length, endIndex)
            .trim()
    }
}