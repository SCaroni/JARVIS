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
import androidx.compose.foundation.border
import androidx.compose.ui.input.key.*
import androidx.compose.foundation.lazy.rememberLazyListState

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

    val history = remember { mutableStateListOf<String>() }

    var historyIndex by remember { mutableStateOf(-1) }

    var input by remember { mutableStateOf("") }

    val messages = remember { mutableStateListOf<String>() }

    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        listState.animateScrollToItem(messages.size)
    }

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
            state = listState,
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
                placeholder = {
                    Text(
                        "digite um comando...",
                        color = TerminalText.copy(alpha = 0.3f)
                    )
                },

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

                modifier = Modifier
                    .weight(1f)
                    .border(
                        width = 1.dp,
                        color = TerminalGreen
                    )
                    .padding(horizontal = 8.dp)
                    .onPreviewKeyEvent { event ->

                        if (event.type == KeyEventType.KeyDown) {

                            if (event.key == Key.DirectionUp) {

                                if (historyIndex > 0) {
                                    historyIndex--
                                    input = history[historyIndex]
                                }

                                true

                            } else if (event.key == Key.DirectionDown) {

                                if (historyIndex < history.size - 1) {
                                    historyIndex++
                                    input = history[historyIndex]
                                } else {
                                    historyIndex = history.size
                                    input = ""
                                }

                                true

                            } else {
                                false
                            }

                        } else {
                            false
                        }
                    }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {

                    if (input.isNotBlank()) {

                        if (input.trim().uppercase() == "CLS") {

                            messages.clear()
                            input = ""
                            return@Button

                        }

                        if (history.isEmpty() || history.last() != input) {
                            history.add(input)
                        }

                        if (history.size > 50) {
                            history.removeAt(0)
                        }

                        historyIndex = history.size

                        val response = processor.handle(input)

                        messages.add("USER: $input")
                        messages.add("JARVIS: $response")

                        input = ""
                    }
                }
            ) {
                Text("Enviar")
            }

        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {

                val response = processor.handle("LIST")

                messages.add("USER: LIST")
                messages.add("JARVIS: $response")

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mostrar Memória")
        }

    }
}