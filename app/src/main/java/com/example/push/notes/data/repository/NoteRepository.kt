package com.example.push.notes.data.repository

import com.example.push.core.network.RetrofitHelper
import com.example.push.notes.data.model.NoteRequest
import com.example.push.notes.data.model.NoteResponse
import retrofit2.HttpException


class NoteRepository {
    private val notesService = RetrofitHelper.notesService
    suspend fun getNotes(): Result<List<NoteResponse>> {
        return try {
            val response = notesService.getNotes()
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Response body is null"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun newNote(request: NoteRequest): Result<NoteResponse> {
        return try {
            val response = notesService.newNote(request)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(HttpException(response))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}





