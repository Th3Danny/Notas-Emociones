package com.example.push.emotion.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.push.emotion.data.repository.EmotionRepository
import com.example.push.emotion.domain.GetWeeklyStatisticsUseCase
import com.example.push.emotion.domain.PostEmotionRecordUseCase
import com.example.push.emotion.domain.PostEmotionUseCase

class EmotionViewModelFactory(
    private val repo: EmotionRepository,
    private val postEmotionUseCase: PostEmotionUseCase,
    private val postEmotionRecordUseCase: PostEmotionRecordUseCase,
    private val getWeeklyStatisticsUseCase: GetWeeklyStatisticsUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EmotionViewModel(repo, postEmotionUseCase, postEmotionRecordUseCase, getWeeklyStatisticsUseCase) as T
    }
}
