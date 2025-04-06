package com.example.push.emotion.data.model

data class EmotionRecordRequest(
    val emotion_id: Int,
    val note: String,
    val intensity: String
)