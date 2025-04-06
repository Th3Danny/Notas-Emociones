package com.example.push.notes.presentation


import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.push.core.data.local.NoteDatabase
import com.example.push.core.data.local.NoteEntity
import com.example.push.notes.data.model.NewNoteRequest

import com.example.push.notes.data.model.NoteResponse
import com.example.push.notes.domain.GetNotesUseCase
import com.example.push.notes.domain.PostNotesUseCase
import com.example.push.worker.scheduleNoteSync
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
            result.onSuccess {
                Log.d("NoteViewModel", "Notas recibidas: ${it.size}")
                _notes.value = it
            }
            result.onFailure {
                Log.e("NoteViewModel", "Error al obtener notas: ${it.message}")
                _error.value = it.message ?: "Error al obtener notas"
            }
        }
    }


    fun createNote(context: Context, request: NewNoteRequest) {
        viewModelScope.launch {
            val result = postNotesUseCase(request)
            result.onSuccess {
                _postSuccess.value = true
            }.onFailure {
                // Guardar en Room local si no hay conexión
                val db = Room.databaseBuilder(
                    context,
                    NoteDatabase::class.java,
                    "note_db"
                ).build()

                val dao = db.noteDao()
                val localNote = NoteEntity(
                    content = request.content,
                    emotionId = request.emotion_id,
                    type = request.type
                )
                dao.insertNote(localNote)

                //  Programar sincronización
                scheduleNoteSync(context)

                _postSuccess.value = true // Se guarda localmente
            }
        }
    }
    fun clearPostSuccess() {
        _postSuccess.value = null
    }



}
