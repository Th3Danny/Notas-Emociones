package com.example.push.emotion.data.model

data class EmotionResponse(
    val id: Int,
    val name: String,
    val description: String,
    val color: String,
    val icon: String
)

data class EmotionListResponse(
    val data: List<EmotionResponse>,
    val message: String,
    val success: Boolean,
    val http_status: String
)

data class NewEmotionResponse(
    val data: EmotionResponse,
    val message: String,
    val success: Boolean,
    val http_status: String
)

