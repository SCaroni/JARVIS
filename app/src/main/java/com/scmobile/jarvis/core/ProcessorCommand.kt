package com.scmobile.jarvis.core

class ProcessorCommand(
    private val engine: JarvisEngine
) {

    fun handle(text: String): String {

        return engine.process(text)

    }
}