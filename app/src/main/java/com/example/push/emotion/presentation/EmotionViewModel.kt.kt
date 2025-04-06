package com.example.push.emotion.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.push.emotion.data.model.EmotionResponse
import com.example.push.emotion.data.model.NewEmotionRequest
import com.example.push.emotion.data.repository.EmotionRepository
import com.example.push.emotion.domain.PostEmotionUseCase
import kotlinx.coroutines.launch

class EmotionViewModel(
    private val repo: EmotionRepository,
    private val postEmotionUseCase: PostEmotionUseCase
) : ViewModel() {

    private val _emotions = MutableLiveData<List<EmotionResponse>>()
    val emotions: LiveData<List<EmotionResponse>> = _emotions

    private val _postSuccess = MutableLiveData<Boolean?>()
    val postSuccess: LiveData<Boolean?> = _postSuccess

    fun loadEmotions() {
        viewModelScope.launch {
            repo.getEmotions().onSuccess {
                _emotions.value = it
            }.onFailure { error ->
                // Opcional: agregar logs
                println(" Error al cargar emociones: ${error.message}")
            }
        }
    }

    fun createEmotion(request: NewEmotionRequest) {
        viewModelScope.launch {
            val result = postEmotionUseCase(request)
            result.onSuccess {
                _postSuccess.value = true
            }.onFailure { error ->
                _postSuccess.value = false
                println(" Error al crear emoción: ${error.message}")
            }
        }
    }
}
