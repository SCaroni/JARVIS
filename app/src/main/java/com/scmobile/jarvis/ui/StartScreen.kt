package com.scmobile.jarvis.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.scmobile.jarvis.ui.theme.TerminalBackground
import com.scmobile.jarvis.ui.theme.TerminalText

@Composable
fun StartScreen(
    onTextMode: () -> Unit,
    onVoiceMode: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TerminalBackground)
            .systemBarsPadding()
    ) {

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(32.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "JARVIS",
                style = MaterialTheme.typography.headlineLarge,
                color = TerminalText
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Cognitive Interface",
                color = TerminalText
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onTextMode,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Modo Texto")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onVoiceMode,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Modo Áudio")
            }

        }
    }
}