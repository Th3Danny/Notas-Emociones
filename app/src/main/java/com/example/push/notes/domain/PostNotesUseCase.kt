package com.example.push.notes.domain

import com.example.push.notes.data.model.NewNoteRequest
import com.example.push.notes.data.model.NewNoteResponse
import com.example.push.notes.data.repository.NoteRepository


class PostNotesUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(noteRequest: NewNoteRequest): Result<NewNoteResponse> {
        return repository.newNote(noteRequest)
    }
}