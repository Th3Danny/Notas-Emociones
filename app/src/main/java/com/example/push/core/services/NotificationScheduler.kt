package com.example.push.core.services

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

fun scheduleDailyNotification(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<DailyNotificationWorker>(
        1, TimeUnit.DAYS
    ).build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "daily_notification_work",
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}