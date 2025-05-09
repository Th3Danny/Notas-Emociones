package com.example.push.notes.data.model


data class NoteListResponse(
    val data: List<NoteResponse>
)

data class NoteResponse(
    val id: Int,
    val content: String,
    val emotion: Emotion,
    val note_date: String,
    val type: String,
    val created_at: String,
    val image_paths: List<String>? = null
)

data class Emotion(
    val id: Int,
    val name: String,
    val description: String,
    val color: String,
    val icon: String
)

data class NewNoteResponse(
    val data: CreatedNote,
    val message: String,
    val success: Boolean,
    val http_status: String
)

data class CreatedNote(
    val id: Int,
    val content: String,
    val emotion: EmotionInNote,
    val note_date: String,
    val type: String,
    val created_at: String
)

data class EmotionInNote(
    val id: Int,
    val name: String,
    val description: String,
    val color: String,
    val icon: String
)
