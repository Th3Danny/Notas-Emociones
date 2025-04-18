package com.example.push.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.push.core.data.local.dao.NoteDao

@Database(entities = [NoteEntity::class], version = 2)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
