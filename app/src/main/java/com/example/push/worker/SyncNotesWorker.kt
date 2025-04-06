package com.example.push.worker

import android.content.Context
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.push.core.data.local.NoteDatabase
import com.example.push.notes.data.model.NewNoteRequest
import com.example.push.notes.data.repository.NoteRepository

class SyncNotesWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        // ✅ Obtener token desde SharedPreferences
        val sharedPrefs = applicationContext.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val token = sharedPrefs.getString("access_token", null)

        if (token.isNullOrEmpty()) {
            return Result.failure() // No se puede sincronizar sin token
        }

        val db = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java,
            "note_db"
        ).build()

        val dao = db.noteDao()
        val notes = dao.getUnsyncedNotes()

        val repository = NoteRepository(token)

        for (note in notes) {
            val result = repository.newNote(NewNoteRequest(note.content, note.emotionId))
            if (result.isSuccess) {
                dao.markAsSynced(note.id)
            }
        }

        return Result.success()
    }
}
