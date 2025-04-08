package com.example.push.notes.domain

import com.example.push.notes.data.model.NewNoteRequest
import com.example.push.notes.data.model.NewNoteResponse
import com.example.push.notes.data.repository.NoteRepository
import java.io.File


class PostNotesUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(
        request: NewNoteRequest,
        imageFiles: List<File> = emptyList()
    ): Result<NewNoteResponse> {
        return repository.newNote(
            content = request.content,
            emotionId = request.emotion_id,
            type = request.type,
            imageFiles = imageFiles
        )
    }
}

