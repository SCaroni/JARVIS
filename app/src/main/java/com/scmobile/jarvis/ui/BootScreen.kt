package com.scmobile.jarvis.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontFamily
import kotlinx.coroutines.delay

import com.scmobile.jarvis.ui.theme.TerminalBackground
import com.scmobile.jarvis.ui.theme.TerminalText

@Composable
fun BootScreen(onBootComplete: () -> Unit) {

    var lines by remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(Unit) {

        val bootLines = listOf(
            "Initializing JARVIS...",
            "",
            "Loading memory modules... OK",
            "Loading command processor... OK",
            "Loading voice interface... OK",
            "",
            "System Ready"
        )

        for (line in bootLines) {
            delay(500)
            lines = lines + line
        }

        delay(800)

        onBootComplete()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TerminalBackground)
            .systemBarsPadding()
            .padding(20.dp)
    ) {

        lines.forEach { line ->

            Text(
                text = line,
                color = TerminalText,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(vertical = 2.dp)
            )

        }

    }
}