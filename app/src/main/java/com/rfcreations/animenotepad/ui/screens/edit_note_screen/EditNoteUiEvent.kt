package com.rfcreations.animenotepad.ui.screens.edit_note_screen

import android.content.Context
import com.rfcreations.animenotepad.database.note.NoteEntity

sealed class EditNoteUiEvent {
    data class SaveNote(val noteToSave: NoteEntity) : EditNoteUiEvent()
    data class RenameNote(val noteToRename: NoteEntity) : EditNoteUiEvent()
    data class ShareNoteAsText(
        val noteTitle: String,
        val noteContent: String,
        val context: Context
    ) : EditNoteUiEvent()

    data class ShareNoteAsFile(
        val noteTitle: String,
        val noteContent: String,
        val context: Context
    ) : EditNoteUiEvent()
}
