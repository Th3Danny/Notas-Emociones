package com.example.push.notes.data.model

data class NoteResponse(
    val id: Int,
    val content: String,
    val emotion: Emotion,
    val note_date: String,
    val type: String,
    val created_at: String
)

data class Emotion(
    val id: Int,
    val name: String,
    val description: String,
    val color: String,
    val icon: String
)

