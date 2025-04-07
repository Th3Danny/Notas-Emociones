package com.example.push.emotion.domain

import com.example.push.emotion.data.model.EmotionRecordRequest
import com.example.push.emotion.data.model.EmotionRecordResponse
import com.example.push.emotion.data.model.EmotionStatisticsResponse
import com.example.push.emotion.data.model.NewEmotionRequest
import com.example.push.emotion.data.model.NewEmotionResponse
import com.example.push.emotion.data.repository.EmotionRepository
import com.example.push.notes.data.model.NoteResponse
import com.example.push.notes.data.repository.NoteRepository


class GetWeeklyStatisticsUseCase(private val repo: EmotionRepository) {
    suspend operator fun invoke(): Result<EmotionStatisticsResponse> {
        return repo.getWeeklyStatistics()
    }
}


class PostEmotionUseCase(private val repository: EmotionRepository) {
    suspend operator fun invoke(request: NewEmotionRequest): Result<NewEmotionResponse> {
        return repository.createEmotion(request)
    }
}

class PostEmotionRecordUseCase(
    private val repository: EmotionRepository
) {
    suspend operator fun invoke(request: EmotionRecordRequest): Result<EmotionRecordResponse> {
        return repository.postEmotionRecord(request)
    }
}