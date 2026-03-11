package com.scmobile.jarvis

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import com.scmobile.jarvis.ui.theme.JarvisTheme
import com.scmobile.jarvis.ui.StartScreen
import com.scmobile.jarvis.ui.ChatScreen
import com.scmobile.jarvis.ui.BootScreen
import com.scmobile.jarvis.ui.VoiceScreen
import com.scmobile.jarvis.memory.JarvisMemory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        checkMicrophonePermission()

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

                    "voice" -> VoiceScreen(memory)

                }

            }
        }
    }

    private fun checkMicrophonePermission() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                1
            )

        }

    }

}