package com.example.push.notes.data.repository

import android.util.Log
import com.example.push.core.network.RetrofitHelper
import com.example.push.notes.data.model.NewNoteRequest
import com.example.push.notes.data.model.NewNoteResponse
import com.example.push.notes.data.model.NoteRequest
import com.example.push.notes.data.model.NoteResponse
import retrofit2.HttpException


class NoteRepository(private val token: String) {
    private val notesService = RetrofitHelper.notesService
    suspend fun getNotes(): Result<List<NoteResponse>> {
        return try {
            val response = notesService.getNotes("Bearer $token")
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
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

    suspend fun newNote(request: NewNoteRequest): Result<NewNoteResponse> {
        return try {
            val response = notesService.newNote("Bearer $token", request)

            if (response.isSuccessful) {
                val body = response.body()
                Log.d("NewNoteRepo", "Nota creada con éxito: ${body?.data?.id}")
                Result.success(body!!)
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





