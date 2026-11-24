package com.miscursos.practica15git.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.miscursos.practica15git.data.model.ReminderItem
import java.text.SimpleDateFormat
import java.util.Locale

object AlarmScheduler {

    private const val TAG = "AlarmScheduler"

    fun scheduleReminder(context: Context, reminder: ReminderItem) {
        if (!reminder.hasAlarm || reminder.isCompleted) {
            cancelReminder(context, reminder.id)
            return
        }

        try {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val dateStr = "${reminder.date} ${reminder.time}"
            val date = format.parse(dateStr) ?: return
            val triggerTime = date.time

            // If time is already in the past, don't schedule
            if (triggerTime <= System.currentTimeMillis()) {
                return
            }

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
            val intent = Intent(context, ReminderNotificationReceiver::class.java).apply {
                putExtra(ReminderNotificationReceiver.EXTRA_ID, reminder.id)
                putExtra(ReminderNotificationReceiver.EXTRA_TITLE, reminder.title)
                putExtra(
                    ReminderNotificationReceiver.EXTRA_DESC,
                    if (reminder.description.isNotBlank()) reminder.description else "Hora programada: ${reminder.time}"
                )
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                reminder.id.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
            Log.d(TAG, "Alarm scheduled for ${reminder.title} at $dateStr")
        } catch (e: Exception) {
            Log.e(TAG, "Error scheduling alarm: ${e.message}", e)
        }
    }

    fun cancelReminder(context: Context, reminderId: Long) {
        try {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
            val intent = Intent(context, ReminderNotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                reminderId.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
            Log.d(TAG, "Alarm cancelled for reminder $reminderId")
        } catch (e: Exception) {
            Log.e(TAG, "Error cancelling alarm: ${e.message}", e)
        }
    }
}
