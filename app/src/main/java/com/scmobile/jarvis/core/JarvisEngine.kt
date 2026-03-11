package com.scmobile.jarvis.core

import com.scmobile.jarvis.command.CommandParser
import com.scmobile.jarvis.command.ExecutorCommand

class JarvisEngine(

    private val parser: CommandParser,
    private val executor: ExecutorCommand

) {

    fun process(input: String): String {

        val command = parser.parse(input)

        return executor.execute(command)
    }
}