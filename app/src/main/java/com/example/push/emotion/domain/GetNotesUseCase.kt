package com.example.push.emotion.domain

import com.example.push.notes.data.model.NoteResponse
import com.example.push.notes.data.repository.NoteRepository


class GetNotesUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(): Result<List<NoteResponse>> {
        return repository.getNotes()
    }
}