package com.rfcreations.animenotepad.database.trash

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrashDao {
    @Insert
    suspend fun insertNoteToTrash(note: TrashEntity)

    @Delete
    suspend fun deleteTrashedNote(note: TrashEntity)

    @Query("SELECT * FROM trash_table")
    fun getAllTrashedNote(): Flow<List<TrashEntity>>

}