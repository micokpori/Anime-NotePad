package com.rfcreations.animenotepad.ui.screens.note_screen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rfcreations.animenotepad.R
import com.rfcreations.animenotepad.database.note.NoteDao
import com.rfcreations.animenotepad.database.note.NoteEntity
import com.rfcreations.animenotepad.database.trash.TrashDao
import com.rfcreations.animenotepad.database.trash.TrashEntity
import com.rfcreations.animenotepad.repository.UserPreferenceRepository
import com.rfcreations.animenotepad.ui.navigation.Screens
import com.rfcreations.animenotepad.utils.Constants
import com.rfcreations.animenotepad.utils.animeImageList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class NoteViewModel @Inject constructor(
    appContext: Application,
    private val noteDao: NoteDao,
    private val trashDao: TrashDao,
    private val userPreferenceRepository: UserPreferenceRepository,
) : ViewModel() {

    private val prefKeys = Constants.PrefKeys
    private val defaultSortMethod = appContext.getString(R.string.default_sort_method)
    val getAllNotes = noteDao.getAllNote()
    private val _sortMethod = MutableStateFlow(
        userPreferenceRepository.getStringPref(
            Constants.PrefKeys.SORT_METHOD_KEY,
            defaultSortMethod
        ) ?: defaultSortMethod
    )
    val sortMethod = _sortMethod.asStateFlow()

    private val _showCreateNewNoteDialog = MutableStateFlow(false)
    val showCreateNewNoteDialog = _showCreateNewNoteDialog.asStateFlow()

    fun uiEvent(event: NoteUiEvent) {
        when (event) {
            is NoteUiEvent.CreateNewNote -> {
                viewModelScope.launch {
                    val navController = event.navController
                    val noteId = UUID.randomUUID().toString()
                    _showCreateNewNoteDialog.value = !_showCreateNewNoteDialog.value
                    noteDao.insertNote(
                        NoteEntity(
                            noteId = noteId,
                            imageId = Random.nextInt(animeImageList.size),
                            title = event.noteTitle,
                            content = ""
                        )
                    )
                    navController.navigate(Screens.EditNoteScreen.route + "/$noteId")
                }
            }

            is NoteUiEvent.ToggleShowCreateNewNoteDialog -> {
                _showCreateNewNoteDialog.value = !_showCreateNewNoteDialog.value
            }
            is NoteUiEvent.MoveNoteToTrash -> {
                viewModelScope.launch {
                    val note = event.noteToTrash
                    trashDao.insertNoteToTrash(
                        TrashEntity(
                            trashNoteId = note.noteId,
                            imageId = note.imageId,
                            title = note.title,
                            content = note.content
                        )
                    )
                    noteDao.deleteNote(event.noteToTrash)
                }
            }
            is NoteUiEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteDao.deleteNote(event.noteToDelete)
                }
            }
            is NoteUiEvent.ChangeImage -> {
                viewModelScope.launch {
                    noteDao.insertNote(event.note)
                }
            }
            is NoteUiEvent.RenameNote -> {
                viewModelScope.launch {
                    val noteToRename = event.note
                    noteDao.insertNote(noteToRename)
                }
            }
            is NoteUiEvent.ChangeSortMethod -> {
                viewModelScope.launch {
                    val newSortMethod = event.newSortMethod
                    _sortMethod.value = newSortMethod
                    userPreferenceRepository.editStringPref(prefKeys.SORT_METHOD_KEY, newSortMethod)
                }
            }
            is NoteUiEvent.NavToEditNoteScreen -> {
                viewModelScope.launch {
                    val navController = event.navController
                    val noteId = event.noteId
                    navController.navigate(Screens.EditNoteScreen.route + "/${noteId}") {
                        popUpTo(Screens.EditNoteScreen.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }

    }

}