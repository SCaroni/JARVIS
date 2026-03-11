package com.scmobile.jarvis.command

import com.scmobile.jarvis.EnumCommand

class CommandParser {

    fun parse(raw: String): JarvisCommand {

        val upper = raw.trim().uppercase()

        return when {

            upper.startsWith("SAVE") -> {
                JarvisCommand(EnumCommand.SAVE, raw)
            }

            upper.startsWith("READ") -> {
                JarvisCommand(EnumCommand.READ, raw)
            }

            upper.startsWith("LIST") -> {
                JarvisCommand(EnumCommand.LIST)
            }

            upper.startsWith("DELETE") -> {
                JarvisCommand(EnumCommand.DELETE, raw)
            }

            upper.startsWith("HELP") -> {
                JarvisCommand(EnumCommand.HELP)
            }

            upper.startsWith("CLEAR") || upper.startsWith("CLS") -> {
                JarvisCommand(EnumCommand.CLEAR)
            }

            upper.startsWith("COUNT") -> {
                JarvisCommand(EnumCommand.COUNT)
            }

            upper.startsWith("EXISTS") -> {
                JarvisCommand(EnumCommand.EXISTS, raw)
            }

            upper.startsWith("VERSION") -> {
                JarvisCommand(EnumCommand.VERSION)
            }

            else -> {
                JarvisCommand(EnumCommand.UNKNOWN, raw)
            }
        }
    }
}