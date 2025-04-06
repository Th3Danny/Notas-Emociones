package com.example.push.notes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.push.notes.domain.GetNotesUseCase
import com.example.push.notes.domain.PostNotesUseCase

class NoteViewModelFactory(
    private val getNotesUseCase: GetNotesUseCase,
    private val postNotesUseCase: PostNotesUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteViewModel(getNotesUseCase, postNotesUseCase) as T
    }
}