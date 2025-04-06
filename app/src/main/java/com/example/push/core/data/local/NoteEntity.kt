package com.example.push.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val content: String,
    val emotionId: Int,
    val type: String = "SITUATION",
    val synced: Boolean = false // ← importante para sincronización
)



