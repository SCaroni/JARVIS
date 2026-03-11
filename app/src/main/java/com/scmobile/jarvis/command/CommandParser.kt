package com.scmobile.jarvis.command

import com.scmobile.jarvis.EnumCommand

class CommandParser {

    fun parse(raw: String): JarvisCommand {

        val upper = raw.trim().uppercase()

        return when {

            upper.startsWith("JARVIS SAVE") -> {
                JarvisCommand(EnumCommand.SAVE, raw)
            }

            upper.startsWith("JARVIS READ") -> {
                JarvisCommand(EnumCommand.READ, raw)
            }

            upper.startsWith("JARVIS LIST") -> {
                JarvisCommand(EnumCommand.LIST)
            }

            upper.startsWith("JARVIS DELETE") -> {
                JarvisCommand(EnumCommand.DELETE, raw)
            }

            else -> {
                JarvisCommand(EnumCommand.UNKNOWN, raw)
            }
        }
    }
}