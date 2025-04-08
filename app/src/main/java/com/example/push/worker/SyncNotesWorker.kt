package com.example.push.worker

import android.content.Context
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.push.core.data.local.NoteDatabase
import com.example.push.core.network.RetrofitHelper
import com.example.push.core.session.SessionManager
import com.example.push.notes.data.model.NewNoteRequest
import com.example.push.notes.data.repository.NoteRepository
import java.io.File

class SyncNotesWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        // Obtener token desde SharedPreferences
        val sharedPrefs = applicationContext.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val token = sharedPrefs.getString("access_token", null)

        if (token.isNullOrEmpty()) {
            return Result.failure() // No se puede sincronizar sin token
        }

        val db = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java,
            "note_db"
        ).fallbackToDestructiveMigration()
            .build()

        // 💡 Inicializa Retrofit aquí también
        val sessionManager = SessionManager(applicationContext)
        RetrofitHelper.initialize(sessionManager)


        val dao = db.noteDao()
        val notes = dao.getUnsyncedNotes()

        val repository = NoteRepository()

        for (note in notes) {
            val imageFiles = note.imagePaths
                ?.split(",")
                ?.mapNotNull { path ->
                    val file = File(path)
                    if (file.exists()) file else null
                } ?: emptyList()

            val result = repository.newNote(
                content = note.content,
                emotionId = note.emotionId,
                type = note.type,
                imageFiles = imageFiles
            )

            if (result.isSuccess) {
                dao.markAsSynced(note.id)

                // Eliminar imágenes temporales después de sincronizar
                imageFiles.forEach { it.delete() }
            }
        }

        db.close()
        return Result.success()
    }
}