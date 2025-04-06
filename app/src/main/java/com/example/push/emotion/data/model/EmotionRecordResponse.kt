package com.example.push.emotion.data.model

data class EmotionRecordResponse(
    val data: EmotionRecordData,
    val message: String,
    val success: Boolean,
    val http_status: String
)

data class EmotionRecordData(
    val id: Int,
    val emotion: EmotionResponse,
    val record_date: String,
    val record_time: String,
    val note: String,
    val intensity: String
)