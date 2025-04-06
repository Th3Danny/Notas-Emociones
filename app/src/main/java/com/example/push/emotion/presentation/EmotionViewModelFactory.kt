package com.example.push.emotion.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.push.emotion.data.repository.EmotionRepository

class EmotionViewModelFactory(
    private val repository: EmotionRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EmotionViewModel(repository) as T
    }
}
