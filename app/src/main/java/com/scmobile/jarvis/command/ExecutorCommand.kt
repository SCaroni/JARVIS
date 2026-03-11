package com.scmobile.jarvis.command

import com.scmobile.jarvis.EnumCommand
import com.scmobile.jarvis.memory.JarvisMemory

class ExecutorCommand(
    private val memory: JarvisMemory
) {

    fun execute(command: JarvisCommand): String {

        return when (command.command) {

            EnumCommand.SAVE -> save(command.content)

            EnumCommand.READ -> read(command.content)

            EnumCommand.LIST -> list()

            EnumCommand.DELETE -> delete(command.content)

            else -> "Comando desconhecido"
        }
    }

    private fun save(content: String): String {

        val line = content
            .replace("JARVIS SAVE", "")
            .trim()

        val parts = line.split("=")

        if (parts.size != 2) {
            return "Formato inválido"
        }

        val token = parts[0].trim()
        val value = parts[1].trim()

        memory.add(token, value)

        return "Salvo: $token"
    }

    private fun read(content: String): String {

        val token = content
            .replace("JARVIS READ", "")
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
            .replace("JARVIS DELETE", "")
            .trim()

        memory.delete(token)

        return "Removido: $token"
    }
}