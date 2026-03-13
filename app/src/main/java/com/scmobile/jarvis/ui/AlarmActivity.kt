package com.scmobile.jarvis.reminder

import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Bundle
import android.os.Vibrator
import android.os.VibrationEffect
import android.os.Build

import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.scmobile.jarvis.ui.theme.TerminalGreen
import com.scmobile.jarvis.ui.theme.TerminalBackground
import com.scmobile.jarvis.ui.theme.TerminalText

class AlarmActivity : ComponentActivity() {

    private var player: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val token = intent.getStringExtra("token") ?: "Reminder"

        startAlarm()

        onBackPressedDispatcher.addCallback(this) {
            // bloqueia gesto de voltar
        }

        setContent {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(TerminalBackground),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "JARVIS ALERT",
                        color = TerminalGreen,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 30.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "----------------------",
                        color = TerminalGreen,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "REMINDER:",
                        color = TerminalGreen,
                        fontFamily = FontFamily.Monospace
                    )

                    Text(
                        text = token,
                        color = TerminalGreen,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 22.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "ALERT LEVEL: HIGH",
                        color = TerminalGreen,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TerminalGreen,
                                contentColor = TerminalBackground
                            ),
                            onClick = {
                                stopAlarm()
                                finish()
                            }
                        ) {
                            Text(
                                "PARAR",
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TerminalGreen,
                                contentColor = TerminalBackground
                            ),
                            onClick = {
                                snooze(token)
                                stopAlarm()
                                finish()
                            }
                        ) {
                            Text(
                                "SONECA 5 MIN",
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
    }

    private fun startAlarm() {

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        player = MediaPlayer.create(this, alarmSound)
        player?.isLooping = true
        player?.start()

        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(
                VibrationEffect.createWaveform(
                    longArrayOf(0, 1000, 1000),
                    0
                )
            )
        } else {
            vibrator?.vibrate(longArrayOf(0, 1000, 1000), 0)
        }
    }

    private fun stopAlarm() {

        player?.stop()
        player?.release()
        player = null

        vibrator?.cancel()
    }

    private fun snooze(token: String) {

        val timestamp = System.currentTimeMillis() + 5 * 60 * 1000

        val reminder = Reminder(token, timestamp)

        ReminderManager(this).schedule(reminder)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAlarm()
    }

}