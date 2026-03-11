package com.scmobile.jarvis

import android.os.Bundle

import com.scmobile.jarvis.ui.theme.JarvisTheme
import com.scmobile.jarvis.ui.StartScreen
import com.scmobile.jarvis.ui.ChatScreen
import com.scmobile.jarvis.memory.JarvisMemory
import com.scmobile.jarvis.ui.BootScreen

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            JarvisTheme {

                val memory = remember { JarvisMemory(this) }

                var screen by remember { mutableStateOf("boot") }

                when (screen) {

                    "boot" -> BootScreen {
                        screen = "start"
                    }

                    "start" -> StartScreen(
                        onTextMode = { screen = "chat" },
                        onVoiceMode = { screen = "voice" }
                    )

                    "chat" -> ChatScreen(memory)
                }

            }
        }
    }
}