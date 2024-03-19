package com.rfcreations.animenotepad.database.note

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query("SELECT * FROM notes_table")
    fun getAllNote(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes_table WHERE noteId=:id")
    fun getNoteById(id : String): NoteEntity
}