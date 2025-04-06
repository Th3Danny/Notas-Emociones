package com.example.push.emotion.data.model

data class EmotionRequest(
    val content: String,
    val emotion_id: Int,
    val type: String = "SITUATION"
)

