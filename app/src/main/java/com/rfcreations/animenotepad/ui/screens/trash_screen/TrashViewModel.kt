package com.rfcreations.animenotepad.ui.screens.trash_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rfcreations.animenotepad.database.note.NoteDao
import com.rfcreations.animenotepad.database.note.NoteEntity
import com.rfcreations.animenotepad.database.trash.TrashDao
import com.rfcreations.animenotepad.database.trash.TrashEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrashViewModel @Inject constructor(private val trashDao: TrashDao, private val noteDao: NoteDao): ViewModel() {

    fun getAllTrashedNotes(): Flow<List<TrashEntity>> {
        return trashDao.getAllTrashedNote()
    }

    fun uiEvent(onEvent: TrashUiEvent){
        when(onEvent){
            is TrashUiEvent.RestoreNote -> {
                viewModelScope.launch {
                    val note = onEvent.noteToRestore
                    noteDao.insertNote(
                        NoteEntity(
                            noteId = note.trashNoteId,
                            imageId = note.imageId,
                            title = note.title,
                            content = note.content
                        )
                    )
                    trashDao.deleteTrashedNote(note)
                }
            }

            is TrashUiEvent.PermanentDeleteTrashNote -> {
                viewModelScope.launch {
                    trashDao.deleteTrashedNote(onEvent.noteToDelete)
                }
            }
        }
    }

}