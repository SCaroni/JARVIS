package com.scmobile.jarvis.command

object CommandRegistry {

    private val commands = mutableListOf<String>()

    fun register(command: String) {
        commands.add(command)
    }

    fun list(): List<String> {
        return commands
    }

}