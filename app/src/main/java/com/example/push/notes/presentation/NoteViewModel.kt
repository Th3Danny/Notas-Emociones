package com.example.push.notes.presentation


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.push.notes.data.model.NewNoteRequest
import com.example.push.notes.data.model.NoteRequest
import com.example.push.notes.data.model.NoteResponse
import com.example.push.notes.domain.GetNotesUseCase
import com.example.push.notes.domain.PostNotesUseCase
import kotlinx.coroutines.launch


class NoteViewModel(
    private val getNotesUseCase: GetNotesUseCase,
    private val postNotesUseCase: PostNotesUseCase // ← nuevo
) : ViewModel() {

    private val _notes = MutableLiveData<List<NoteResponse>>()
    val notes: LiveData<List<NoteResponse>> = _notes

    private val _postSuccess = MutableLiveData<Boolean?>()
    val postSuccess: LiveData<Boolean?> = _postSuccess

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getNotes() {
        viewModelScope.launch {
            val result = getNotesUseCase()
            result.onSuccess { _notes.value = it }
            result.onFailure { _error.value = it.message ?: "Error al obtener notas" }
        }
    }

    fun createNote(request: NewNoteRequest) {
        viewModelScope.launch {
            val result = postNotesUseCase(request)
            result.onSuccess {
                _postSuccess.value = true
            }
            result.onFailure {
                _postSuccess.value = false
                _error.value = it.message ?: "Error al crear la nota"
            }
        }
    }
}
