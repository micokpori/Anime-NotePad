package com.rfcreations.animenotepad.database.note

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "notes_table")
data class NoteEntity(
    @PrimaryKey val noteId: String,
    @ColumnInfo(name = "imageId") val imageId: Int,
    @ColumnInfo(name = "note_title") val title: String,
    @ColumnInfo(name = "note_content") val content: String,
    @ColumnInfo(name = "last_modified") val lastModified: Long = Calendar.getInstance().timeInMillis,
    //the  below row will be implemented on future updates
    @ColumnInfo(name = "img_path") val imgPath: String? = null
)