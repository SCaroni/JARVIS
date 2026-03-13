package com.scmobile.jarvis.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class ReminderManager(private val context: Context) {

    fun schedule(reminder: Reminder) {

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("token", reminder.token)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.token.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {

            val info = AlarmManager.AlarmClockInfo(
                reminder.timestamp,
                pendingIntent
            )

            alarmManager.setAlarmClock(info, pendingIntent)

        } catch (e: SecurityException) {

            e.printStackTrace()

        }
    }
}