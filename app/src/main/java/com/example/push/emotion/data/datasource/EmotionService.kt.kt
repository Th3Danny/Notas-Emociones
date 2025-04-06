package com.example.push.emotion.data.datasource

import com.example.push.emotion.data.model.EmotionListResponse
import com.example.push.emotion.data.model.EmotionResponse
import com.example.push.emotion.data.model.NewEmotionRequest
import com.example.push.emotion.data.model.NewEmotionResponse
import com.example.push.notes.data.model.NoteRequest
import com.example.push.notes.data.model.NoteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface EmotionService {
    @GET("emotions")
    suspend fun getEmotions(
        @Header("Authorization") token: String
    ): Response<EmotionListResponse>

    @POST("emotions")
    suspend fun createEmotion(
        @Header("Authorization") token: String,
        @Body request: NewEmotionRequest
    ): Response<NewEmotionResponse>
}