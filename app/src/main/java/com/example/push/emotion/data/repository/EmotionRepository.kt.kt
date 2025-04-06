package com.example.push.emotion.data.repository

import android.util.Log
import com.example.push.core.network.RetrofitHelper
import com.example.push.emotion.data.model.EmotionResponse
import com.example.push.notes.data.model.NoteRequest
import com.example.push.notes.data.model.NoteResponse
import retrofit2.HttpException


class EmotionRepository(private val token: String) {
    private val service = RetrofitHelper.emotionService

    suspend fun getEmotions(): Result<List<EmotionResponse>> {
        return try {
            Log.d("EmotionRepo", "Llamando a la API con token...")
            val response = service.getEmotions("Bearer $token")

            if (response.isSuccessful) {
                val body = response.body()
                Log.d("EmotionRepo", "Emociones recibidas: ${body?.data?.size}")
                Result.success(body?.data ?: emptyList())
            } else {
                val error = response.errorBody()?.string()
                Log.e("EmotionRepo", "Error en la respuesta: $error")
                Result.failure(Exception("Error HTTP ${response.code()} - $error"))
            }
        } catch (e: Exception) {
            Log.e("EmotionRepo", "Excepción al obtener emociones: ${e.message}", e)
            Result.failure(e)
        }
    }
}






