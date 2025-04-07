package com.example.push.emotion.data.model

data class EmotionStatisticsResponse(
    val data: EmotionStatisticsData
)

data class EmotionStatisticsData(
    val emotion_counts: Map<String, Int>,
    val emotion_colors: Map<String, String>,
    val daily_emotions: List<DailyEmotion>
)

data class DailyEmotion(
    val date: String,
    val emotions: Map<String, Int>
)
