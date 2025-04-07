package com.example.push.emotion.data.repository

import android.util.Log
import com.example.push.core.network.RetrofitHelper
import com.example.push.emotion.data.model.EmotionRecordRequest
import com.example.push.emotion.data.model.EmotionRecordResponse
import com.example.push.emotion.data.model.EmotionResponse
import com.example.push.emotion.data.model.EmotionStatisticsResponse
import com.example.push.emotion.data.model.NewEmotionRequest
import com.example.push.emotion.data.model.NewEmotionResponse
import com.example.push.notes.data.model.NoteRequest
import com.example.push.notes.data.model.NoteResponse
import retrofit2.HttpException


class EmotionRepository() {
    private val service = RetrofitHelper.emotionService

    suspend fun getEmotions(): Result<List<EmotionResponse>> {
        Log.d("EmotionRepo", "Llamando a la API con token...")
        return try {
            val response = service.getEmotions()
            if (response.isSuccessful) {
                Log.d("EmotionRepo", "Emociones cargadas con éxito")
                Result.success(response.body()?.data ?: emptyList())
            } else {
                Log.e("EmotionRepo", "Error en la respuesta: ${response.errorBody()?.string()}")
                Result.failure(Exception("Error HTTP ${response.code()} - ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Log.e("EmotionRepo", "Excepción al obtener emociones", e)
            Result.failure(e)
        }
    }

    suspend fun getWeeklyStatistics(): Result<EmotionStatisticsResponse> {
        return try {
            val response = service.getWeeklyStatistics()
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error HTTP: ${response.code()} - ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun createEmotion(request: NewEmotionRequest): Result<NewEmotionResponse> {
        return try {
            val response = service.createEmotion(request)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error HTTP: ${response.code()} - ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Log.e("EmotionRepo", "Error al crear emoción: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun postEmotionRecord(request:EmotionRecordRequest): Result<EmotionRecordResponse> {
        return try {
            val response = service.postEmotionRecord(request)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error HTTP: ${response.code()} - ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Log.e("EmotionRecordRepo", "Error al registrar emoción: ${e.message}")
            Result.failure(e)
        }
    }


}






