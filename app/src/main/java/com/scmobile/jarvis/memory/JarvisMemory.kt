package com.scmobile.jarvis.memory

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import com.scmobile.jarvis.model.MemoryItem

class JarvisMemory(private val context: Context) {

    private val fileName = "jarvis_memory.json"
    private val gson = Gson()

    private fun getFile(): File {
        return File(context.filesDir, fileName)
    }

    fun load(): MutableList<MemoryItem> {
        val file = getFile()

        if (!file.exists()) return mutableListOf()

        val json = file.readText()

        val type = object : TypeToken<MutableList<MemoryItem>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    fun save(list: List<MemoryItem>) {
        val json = gson.toJson(list)
        getFile().writeText(json)
    }

    fun add(token: String, value: String) {

        val memory = load()

        memory.removeAll { it.token == token }

        memory.add(
            MemoryItem(
                token = token,
                value = value
            )
        )

        save(memory)
    }

    fun read(token: String): MemoryItem? {
        return load().find { it.token == token }
    }

    fun list(): List<MemoryItem> {
        return load()
    }

    fun delete(token: String) {

        val memory = load()

        memory.removeAll { it.token == token }

        save(memory)
    }

    fun clear() {

        val memory = load()

        memory.clear()

        save(memory)

    }
}