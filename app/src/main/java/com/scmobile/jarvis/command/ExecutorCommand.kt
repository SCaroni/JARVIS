package com.scmobile.jarvis.command

import com.scmobile.jarvis.EnumCommand
import com.scmobile.jarvis.memory.JarvisMemory
import com.scmobile.jarvis.reminder.Reminder
import com.scmobile.jarvis.reminder.ReminderManager

import android.content.Context

import java.time.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

import android.util.Log  //teste

class ExecutorCommand(
    private val memory: JarvisMemory,
    private val context: Context
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
        CommandRegistry.register("DELETE TOKEN / DELETE ALL")
        CommandRegistry.register("TIME TOKEN DD/MM HH:MM")
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

            EnumCommand.TIME -> time(command.content)

            EnumCommand.CLEAR -> ""

            else -> "Comando desconhecido\nDigite HELP para ver os comandos disponíveis."
        }
    }

    fun help(): String {

        return """
JARVIS COMMANDS
----------------

[MEMORY]
SAVE TOKEN = value
READ TOKEN
LIST
DELETE TOKEN
COUNT
EXISTS TOKEN

[SYSTEM]
VERSION
CLS / CLEAR
HELP
""".trimIndent()

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

        if (token.uppercase() == "ALL" || token == "*") {

            memory.clear()

            return "Toda a memória foi apagada."

        }

        val existing = memory.read(token)

        if (existing == null) {
            return "Token não encontrado"
        }

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

    private fun time(content: String): String {

        val parts = content
            .replace("TIME", "")
            .trim()
            .split(" ")

        if (parts.size == 2 && parts[1].startsWith("+")) {

            val token = parts[0]
            val relative = parts[1].substring(1)

            val now = System.currentTimeMillis()

            val timestamp = when {
                relative.endsWith("s") -> now + relative.dropLast(1).toLong() * 1000
                relative.endsWith("m") -> now + relative.dropLast(1).toLong() * 60_000
                relative.endsWith("h") -> now + relative.dropLast(1).toLong() * 3_600_000
                else -> return "Formato inválido. Use +10m, +2h ou +30s"
            }

            Log.d("JARVIS", "Reminder criado para timestamp: $timestamp")

            val reminder = Reminder(token, timestamp)

            ReminderManager(context).schedule(reminder)

            return "Reminder criado em $relative"
        }

        if (parts.size == 3 && parts[1].equals("amanhã", true)) {

            val token = parts[0]
            val hour = parts[2]

            val formatter = DateTimeFormatter.ofPattern("HH:mm")

            val time = LocalTime.parse(hour, formatter)

            val dateTime = LocalDate.now()
                .plusDays(1)
                .atTime(time)

            val timestamp = dateTime
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()

            ReminderManager(context).schedule(Reminder(token, timestamp))

            return "Reminder criado para amanhã às $hour"
        }

        if (parts.size != 3) {
            return "Formato: TIME token dd/MM HH:mm"
        }

        val token = parts[0]
        val date = parts[1]
        val hour = parts[2]

        try {

            val year = LocalDateTime.now().year

            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

            val dateTime = LocalDateTime.parse("$date/$year $hour", formatter)

            val timestamp = dateTime
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()

            val reminder = Reminder(
                token = token,
                timestamp = timestamp
            )

            ReminderManager(context).schedule(reminder)

            return "Reminder criado: $token → $date $hour"

        } catch (_: Exception) {

            return "Data inválida. Use: TIME token dd/MM HH:mm"

        }

    }

    private fun version(): String {

        return """
JARVIS Cognitive Interface
Build 0.1
""".trimIndent()

    }
}