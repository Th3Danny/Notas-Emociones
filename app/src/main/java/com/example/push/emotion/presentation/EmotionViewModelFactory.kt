package com.example.push.emotion.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.push.emotion.data.repository.EmotionRepository
import com.example.push.emotion.domain.PostEmotionUseCase

class EmotionViewModelFactory(
    private val repo: EmotionRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EmotionViewModel(
            repo = repo,
            postEmotionUseCase = PostEmotionUseCase(repo)
        ) as T
    }
}
