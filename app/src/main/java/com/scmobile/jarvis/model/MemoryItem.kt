package com.scmobile.jarvis.model

data class MemoryItem(
    val token: String,
    val value: String,
    val timestamp: Long = System.currentTimeMillis()
)