package com.example.push.notes.data.model

data class NewNoteRequest(
    val content: String,
    val emotion_id: Int,
    val type: String = "SITUATION"
)

