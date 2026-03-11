package com.scmobile.jarvis.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background

import com.scmobile.jarvis.command.CommandParser
import com.scmobile.jarvis.command.ExecutorCommand
import com.scmobile.jarvis.core.JarvisEngine
import com.scmobile.jarvis.core.ProcessorCommand
import com.scmobile.jarvis.memory.JarvisMemory

import com.scmobile.jarvis.ui.theme.TerminalDivider
import com.scmobile.jarvis.ui.theme.TerminalText
import com.scmobile.jarvis.ui.theme.TerminalPrompt
import com.scmobile.jarvis.ui.theme.TerminalGreen
import com.scmobile.jarvis.ui.theme.TerminalBackground

@Composable
fun ChatScreen(memory: JarvisMemory) {

    val parser = remember { CommandParser() }
    val executor = remember { ExecutorCommand(memory) }
    val engine = remember { JarvisEngine(parser, executor) }
    val processor = remember { ProcessorCommand(engine) }

    var input by remember { mutableStateOf("") }

    val messages = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TerminalBackground)
            .systemBarsPadding()
            .padding(16.dp)
    ) {

        Text(
            text = "JARVIS Cognitive Interface",
            style = MaterialTheme.typography.titleLarge,
            color = TerminalText
        )

        Text(
            text = "Build 0.1",
            style = MaterialTheme.typography.bodyMedium,
            color = TerminalText
        )

        Divider(
            color = TerminalDivider,
            thickness = 1.dp
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

            items(messages) { msg ->

                Text(
                    text = msg,
                    color = TerminalText,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )

            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "C:\\JARVIS> ",
                color = TerminalPrompt,
                fontFamily = FontFamily.Monospace
            )

            TextField(
                value = input,
                onValueChange = { input = it },

                textStyle = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    color = TerminalText
                ),

                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = TerminalGreen
                ),

                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {

                    if (input.trim().equals("cls", ignoreCase = true)) {
                        messages.clear()
                        input = ""
                        return@Button
                    }

                    val response = processor.handle(input)

                    messages.add("C:\\JARVIS> $input")
                    messages.add(response)

                    input = ""

                }
            ) {
                Text("Enviar")
            }

        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {

                val response = processor.handle("JARVIS LIST")

                messages.add(response)

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mostrar Memória")
        }

    }
}