package com.example.push.notes.domain

import com.example.push.notes.data.model.NoteRequest
import com.example.push.notes.data.model.NoteResponse
import com.example.push.notes.data.repository.NoteRepository


class PostNotesUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(noteRequest: NoteRequest): Result<NoteResponse> {
        return repository.newNote(noteRequest)
    }
}