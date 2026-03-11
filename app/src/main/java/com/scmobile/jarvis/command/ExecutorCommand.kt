package com.scmobile.jarvis.command

import com.scmobile.jarvis.EnumCommand
import com.scmobile.jarvis.memory.JarvisMemory

class ExecutorCommand(
    private val memory: JarvisMemory
) {

    init {

        CommandRegistry.register("SAVE TOKEN = value")
        CommandRegistry.register("READ TOKEN")
        CommandRegistry.register("LIST")
        CommandRegistry.register("DELETE TOKEN")
        CommandRegistry.register("COUNT")
        CommandRegistry.register("EXISTS TOKEN")
        CommandRegistry.register("VERSION")
        CommandRegistry.register("CLS / CLEAR")
        CommandRegistry.register("HELP")

    }

    fun execute(command: JarvisCommand): String {

        return when (command.command) {

            EnumCommand.SAVE -> save(command.content)

            EnumCommand.READ -> read(command.content)

            EnumCommand.LIST -> list()

            EnumCommand.DELETE -> delete(command.content)

            EnumCommand.COUNT -> count()

            EnumCommand.EXISTS -> exists(command.content)

            EnumCommand.VERSION -> version()

            EnumCommand.HELP -> help()

            EnumCommand.CLEAR -> ""

            else -> "Comando desconhecido\nDigite HELP para ver os comandos disponíveis."
        }
    }

    fun help(): String {

        val builder = StringBuilder()

        builder.appendLine("JARVIS COMMANDS")
        builder.appendLine("----------------")

        CommandRegistry.list().forEach {
            builder.appendLine(it)
        }

        return builder.toString()
    }

    private fun save(content: String): String {

        val line = content
            .replace("SAVE", "")
            .trim()

        val token: String
        val value: String

        if (line.contains("=")) {

            val parts = line.split("=")

            if (parts.size != 2) {
                return "Formato inválido"
            }

            token = parts[0].trim()
            value = parts[1].trim()

        } else if (line.contains(" é ")) {

            val parts = line.split(" é ")

            if (parts.size != 2) {
                return "Formato inválido"
            }

            token = parts[0].trim()
            value = parts[1].trim()

        } else {

            return "Formato inválido. Use '=' ou 'é'."

        }

        memory.add(token, value)

        return "Salvo: $token"
    }
    private fun read(content: String): String {

        val token = content
            .replace("READ", "")
            .trim()

        val result = memory.read(token)

        return result?.value ?: "Token não encontrado"
    }

    private fun list(): String {

        val items = memory.list()

        if (items.isEmpty()) {
            return "Memória vazia"
        }

        return items.joinToString("\n") {
            "${it.token} → ${it.value}"
        }
    }

    private fun delete(content: String): String {

        val token = content
            .replace("DELETE", "")
            .trim()

        memory.delete(token)

        return "Removido: $token"
    }

    private fun count(): String {

        val size = memory.list().size

        return "Memory items: $size"
    }

    private fun exists(content: String): String {

        val token = content
            .replace("EXISTS", "")
            .trim()

        val result = memory.read(token)

        return if (result != null) "TRUE" else "FALSE"
    }

    private fun version(): String {

        return """
JARVIS Cognitive Interface
Build 0.1
""".trimIndent()

    }
}