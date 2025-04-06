package com.example.push.emotion.presentation


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.push.emotion.data.model.EmotionResponse
import com.example.push.emotion.data.repository.EmotionRepository
import com.example.push.notes.data.model.NoteRequest
import com.example.push.notes.data.model.NoteResponse
import com.example.push.notes.domain.GetNotesUseCase
import com.example.push.notes.domain.PostNotesUseCase
import kotlinx.coroutines.launch


class EmotionViewModel(private val repo: EmotionRepository) : ViewModel() {
    private val _emotions = MutableLiveData<List<EmotionResponse>>()
    val emotions: LiveData<List<EmotionResponse>> = _emotions

    fun loadEmotions() {
        viewModelScope.launch {
            repo.getEmotions().onSuccess {
                _emotions.value = it
            }.onFailure {

            }
        }
    }
}
