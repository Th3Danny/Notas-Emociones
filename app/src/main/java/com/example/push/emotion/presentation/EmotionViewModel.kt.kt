package com.example.push.emotion.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.push.emotion.data.model.EmotionRecordRequest
import com.example.push.emotion.data.model.EmotionResponse
import com.example.push.emotion.data.model.EmotionStatisticsResponse
import com.example.push.emotion.data.model.NewEmotionRequest
import com.example.push.emotion.data.repository.EmotionRepository
import com.example.push.emotion.domain.GetWeeklyStatisticsUseCase
import com.example.push.emotion.domain.PostEmotionRecordUseCase
import com.example.push.emotion.domain.PostEmotionUseCase
import kotlinx.coroutines.launch

class EmotionViewModel(
    private val repo: EmotionRepository,
    private val postEmotionUseCase: PostEmotionUseCase,
    private val postEmotionRecordUseCase: PostEmotionRecordUseCase,
    private val getWeeklyStatisticsUseCase: GetWeeklyStatisticsUseCase
) : ViewModel() {
    private val _weeklyStats = MutableLiveData<EmotionStatisticsResponse?>()
    val weeklyStats: LiveData<EmotionStatisticsResponse?> = _weeklyStats

    private val _emotions = MutableLiveData<List<EmotionResponse>>()
    val emotions: LiveData<List<EmotionResponse>> = _emotions

    private val _success = MutableLiveData<Boolean?>()
    val success: LiveData<Boolean?> = _success

    private val _postSuccess = MutableLiveData<Boolean?>()
    val postSuccess: LiveData<Boolean?> = _postSuccess

    fun loadEmotions() {
        viewModelScope.launch {
            repo.getEmotions().onSuccess {
                _emotions.value = it
            }.onFailure { error ->
                println(" Error al cargar emociones: ${error.message}")
            }
        }
    }

    fun loadWeeklyStats() {
        viewModelScope.launch {
            val result = getWeeklyStatisticsUseCase()
            result.onSuccess { _weeklyStats.value = it }
                .onFailure { println("Error al cargar estadísticas semanales: ${it.message}") }
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

    fun createRecordEmotion(request: EmotionRecordRequest) {
        viewModelScope.launch {
            val result = postEmotionRecordUseCase(request)
            result.onSuccess {
                _postSuccess.value = true
            }.onFailure { error ->
                _postSuccess.value = false
                println(" Error al registar emoción: ${error.message}")
            }
        }
    }



    fun resetStatus() {
        _success.value = null
    }

}
