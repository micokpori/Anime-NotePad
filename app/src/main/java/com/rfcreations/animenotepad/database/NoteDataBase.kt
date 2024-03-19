package com.rfcreations.animenotepad.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rfcreations.animenotepad.database.note.NoteDao
import com.rfcreations.animenotepad.database.note.NoteEntity
import com.rfcreations.animenotepad.database.trash.TrashDao
import com.rfcreations.animenotepad.database.trash.TrashEntity

@Database(entities = [NoteEntity::class, TrashEntity::class], version = 1, exportSchema = false)
abstract class NoteDataBase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun trashDao(): TrashDao
}