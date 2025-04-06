package com.example.push.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.push.core.data.local.NoteEntity


@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Query("SELECT * FROM pending_notes WHERE synced = 0")
    suspend fun getUnsyncedNotes(): List<NoteEntity>

    @Query("UPDATE pending_notes SET synced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: Int)
}
