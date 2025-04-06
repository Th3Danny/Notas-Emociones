package com.example.push.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager

import java.util.concurrent.TimeUnit

object NoteSyncManager {
    fun scheduleSync(context: Context) {
        val request = PeriodicWorkRequestBuilder<SyncNotesWorker>(15, TimeUnit.MINUTES)
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "sync_notes_worker",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}
