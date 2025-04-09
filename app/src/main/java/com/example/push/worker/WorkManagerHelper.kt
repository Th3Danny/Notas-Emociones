package com.example.push.worker

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

fun scheduleNoteSync(context: Context) {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val syncRequest = OneTimeWorkRequestBuilder<SyncNotesWorker>()
        .setConstraints(constraints)
        .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
        .build()

    WorkManager.getInstance(context).enqueueUniqueWork(
        "note_sync_worker",
        ExistingWorkPolicy.KEEP,
        syncRequest
    )
}