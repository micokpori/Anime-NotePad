package com.rfcreations.animenotepad.ui.screens.edit_note_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rfcreations.animenotepad.database.note.NoteDao
import com.rfcreations.animenotepad.repository.UserPreferenceRepository
import com.rfcreations.animenotepad.utils.Constants
import com.rfcreations.animenotepad.utils.ShareNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val noteDao: NoteDao,
    userPreference: UserPreferenceRepository
) : ViewModel() {
    private val prefKeys = Constants.PrefKeys

    fun getNoteById(noteId: String) = noteDao.getNoteById(noteId)

    val saveNoteAutomaticallyPref =
        userPreference.getBooleanPref(Constants.PrefKeys.SAVE_NOTE_AUTOMATICALLY, true)

    val openNoteInReadMode = userPreference.getBooleanPref(prefKeys.OPEN_NOTE_IN_READER_MODE, false)

    fun uiEvent(event: EditNoteUiEvent) {
        when (event) {
            is EditNoteUiEvent.RenameNote -> {
                viewModelScope.launch {
                    val note = event.noteToRename
                    noteDao.insertNote(note)
                }
            }

            is EditNoteUiEvent.SaveNote -> {
                viewModelScope.launch {
                    val note = event.noteToSave
                    noteDao.insertNote(note)
                }
            }

            is EditNoteUiEvent.ShareNoteAsText -> {
                viewModelScope.launch {
                    ShareNote(
                        event.noteTitle,
                        event.noteContent,
                        event.context
                    ).asText()
                }
            }

            is EditNoteUiEvent.ShareNoteAsFile -> {
                viewModelScope.launch {
                    ShareNote(
                        event.noteTitle,
                        event.noteContent,
                        event.context
                    ).asTextFile()
                }
            }
        }
    }
}