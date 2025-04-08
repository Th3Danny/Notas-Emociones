package com.example.push.notes.data.repository

import android.util.Log
import com.example.push.core.network.RetrofitHelper
import com.example.push.notes.data.model.NewNoteResponse
import com.example.push.notes.data.model.NoteResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class NoteRepository() {
    private val notesService = RetrofitHelper.notesService
    suspend fun getNotes(): Result<List<NoteResponse>> {
        return try {
            val response = notesService.getNotes()
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it.data) }
                    ?: Result.failure(Exception("Response body is null"))
            } else {
                val error = response.errorBody()?.string()
                Log.e("NoteRepo", "Error al obtener notas: $error")
                Result.failure(Exception("HTTP ${response.code()} - $error"))
            }
        } catch (e: Exception) {
            Log.e("NoteRepo", "Excepción: ${e.message}", e)
            Result.failure(e)
        }
    }


    suspend fun newNote(
        content: String,
        emotionId: Int,
        type: String = "SITUATION",
        imageFiles: List<File> = emptyList()
    ): Result<NewNoteResponse> {
        return try {
            val contentPart = content.toRequestBody("text/plain".toMediaTypeOrNull())
            val emotionIdPart = emotionId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val typePart = type.toRequestBody("text/plain".toMediaTypeOrNull())

            val imageParts = imageFiles.map { file ->
                val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("images", file.name, requestBody)
            }

            val response = notesService.newNote(contentPart, emotionIdPart, typePart, imageParts)

            if (response.isSuccessful) {
                Log.d("NewNoteRepo", "Nota creada con éxito: ${response.body()?.data?.id}")
                Result.success(response.body()!!)
            } else {
                val error = response.errorBody()?.string()
                Log.e("NewNoteRepo", "Error HTTP al crear nota: $error")
                Result.failure(Exception("HTTP ${response.code()} - $error"))
            }
        } catch (e: Exception) {
            Log.e("NewNoteRepo", "Excepción al crear nota: ${e.message}", e)
            Result.failure(e)
        }
    }

}





