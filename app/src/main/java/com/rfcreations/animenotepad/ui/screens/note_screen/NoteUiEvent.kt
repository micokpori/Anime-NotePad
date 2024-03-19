package com.rfcreations.animenotepad.ui.screens.note_screen

import androidx.navigation.NavController
import com.rfcreations.animenotepad.database.note.NoteEntity

sealed class NoteUiEvent {
    data class CreateNewNote(val noteTitle: String, val navController: NavController) :
        NoteUiEvent()
    data object ToggleShowCreateNewNoteDialog : NoteUiEvent()
    data class MoveNoteToTrash(val noteToTrash: NoteEntity) : NoteUiEvent()
    data class DeleteNote(val noteToDelete: NoteEntity) : NoteUiEvent()
    data class ChangeImage(val note: NoteEntity) : NoteUiEvent()
    data class RenameNote(val note: NoteEntity) : NoteUiEvent()
    data class ChangeSortMethod(val newSortMethod: String) : NoteUiEvent()
    data class NavToEditNoteScreen(val navController: NavController, val noteId: String) :
        NoteUiEvent()
}