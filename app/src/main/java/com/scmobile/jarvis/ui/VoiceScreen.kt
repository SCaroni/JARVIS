package com.scmobile.jarvis.ui

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import com.scmobile.jarvis.command.CommandParser
import com.scmobile.jarvis.command.ExecutorCommand
import com.scmobile.jarvis.core.JarvisEngine
import com.scmobile.jarvis.core.ProcessorCommand
import com.scmobile.jarvis.memory.JarvisMemory
import com.scmobile.jarvis.ui.theme.TerminalBackground
import com.scmobile.jarvis.ui.theme.TerminalText

@Composable
fun VoiceScreen(memory: JarvisMemory) {

    val context = LocalContext.current

    val parser = remember { CommandParser() }
    val executor = remember { ExecutorCommand(memory) }
    val engine = remember { JarvisEngine(parser, executor) }
    val processor = remember { ProcessorCommand(engine) }

    var resultText by remember { mutableStateOf("Pressione o botão e fale com JARVIS") }

    var listening by remember { mutableStateOf(false) }

    val speechRecognizer = remember {
        SpeechRecognizer.createSpeechRecognizer(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TerminalBackground)
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = if (listening) "🎤 JARVIS está ouvindo..." else resultText,
            color = TerminalText
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(

            onClick = {

                listening = true

                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )

                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE,
                    "pt-BR"
                )

                speechRecognizer.setRecognitionListener(object : RecognitionListener {

                    override fun onResults(results: Bundle?) {

                        val matches =
                            results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                        val spokenText = matches?.get(0) ?: ""

                        val response = processor.handle(spokenText)

                        resultText = """
Você disse: $spokenText

Resposta:
$response
""".trimIndent()

                        listening = false
                    }

                    override fun onEndOfSpeech() {
                        listening = false
                    }

                    override fun onError(error: Int) {
                        resultText = "Erro no reconhecimento de voz"
                        listening = false
                    }

                    override fun onReadyForSpeech(params: Bundle?) {}
                    override fun onBeginningOfSpeech() {}
                    override fun onRmsChanged(rmsdB: Float) {}
                    override fun onBufferReceived(buffer: ByteArray?) {}
                    override fun onPartialResults(partialResults: Bundle?) {}
                    override fun onEvent(eventType: Int, params: Bundle?) {}

                })

                speechRecognizer.startListening(intent)

            }

        ) {

            Text(
                if (listening)
                    "🎤 Ouvindo..."
                else
                    "Falar com JARVIS"
            )

        }

    }
}