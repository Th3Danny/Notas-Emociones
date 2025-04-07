package com.example.push.emotion.data.datasource

import com.example.push.emotion.data.model.EmotionListResponse
import com.example.push.emotion.data.model.EmotionRecordRequest
import com.example.push.emotion.data.model.EmotionRecordResponse
import com.example.push.emotion.data.model.EmotionStatisticsResponse
import com.example.push.emotion.data.model.NewEmotionRequest
import com.example.push.emotion.data.model.NewEmotionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface EmotionService {
    @GET("emotions")
    suspend fun getEmotions(
    ): Response<EmotionListResponse>

    @GET("emotion-records/statistics/week")
    suspend fun getWeeklyStatistics(): Response<EmotionStatisticsResponse>


    @POST("emotions")
    suspend fun createEmotion(
        @Body request: NewEmotionRequest
    ): Response<NewEmotionResponse>

    @POST("emotion-records")
    suspend fun postEmotionRecord(
        @Body request: EmotionRecordRequest
    ): Response<EmotionRecordResponse>
}