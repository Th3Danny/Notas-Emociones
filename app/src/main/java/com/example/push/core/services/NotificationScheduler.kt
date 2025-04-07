package com.example.push.core.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

fun scheduleDailyNotification(context: Context) {
    // Crear canal de notificaciones (requerido para Android 8+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelId = "emotion_reminders"
        val name = "Recordatorios de emociones"
        val description = "Recordatorios diarios para registrar tus emociones"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            this.description = description
        }

        // Registrar el canal con el sistema
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // Configurar la solicitud de trabajo periódica para notificaciones diarias
    val dailyNotificationRequest = PeriodicWorkRequestBuilder<DailyNotificationWorker>(
        24, TimeUnit.HOURS  // Repetir cada 24 horas
    ).build()

    // Programar el trabajo con WorkManager
    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "daily_emotion_notification",
        ExistingPeriodicWorkPolicy.KEEP,  // Si ya existe, mantenemos la programación anterior
        dailyNotificationRequest
    )
}