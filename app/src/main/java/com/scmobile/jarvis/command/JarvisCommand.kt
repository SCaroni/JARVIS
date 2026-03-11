package com.scmobile.jarvis.command

import com.scmobile.jarvis.EnumCommand

data class JarvisCommand(

    val command: EnumCommand,
    val content: String = ""

)