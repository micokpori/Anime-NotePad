package com.rfcreations.animenotepad.ui.screens.note_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rfcreations.animenotepad.R
import com.rfcreations.animenotepad.ui.commons.DrawerContent
import com.rfcreations.animenotepad.ui.commons.ExitDialog
import com.rfcreations.animenotepad.ui.commons.NoteSearchField
import com.rfcreations.animenotepad.ui.screens.note_screen.components.CreateNoteDialog
import com.rfcreations.animenotepad.ui.screens.note_screen.components.FloatingButton
import com.rfcreations.animenotepad.ui.screens.note_screen.components.NoteItem
import com.rfcreations.animenotepad.ui.screens.note_screen.components.SortBottomSheet
import com.rfcreations.animenotepad.utils.animeImageList
import com.rfcreations.animenotepad.utils.convertCreationDate
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 *Top level composable
 *Entry home screen
 */
@Composable
fun NotesScreen(
    navController: NavController, noteViewModel: NoteViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val showExitDialog = rememberSaveable { mutableStateOf(false) }
    BackHandler {
        if (drawerState.isOpen) {
            scope.launch { drawerState.close() }
        } else {
            showExitDialog.value = true
        }
    }

    val getAllNotes = noteViewModel.getAllNotes.collectAsState(initial = emptyList()).value
    val sortOptions = stringArrayResource(id = R.array.sort_options)
    val selectedSortMethod = noteViewModel.sortMethod.collectAsState()

    val showCreateNewNoteDialog = noteViewModel.showCreateNewNoteDialog.collectAsState()

    val searchQuery = rememberSaveable { mutableStateOf("") }
    val showSortMethodBottomSheet = rememberSaveable { mutableStateOf(false) }

    val sortByNewest = getAllNotes.sortedBy { note -> note.lastModified }.reversed()
        .filter { filterTitle -> filterTitle.title.contains(searchQuery.value, ignoreCase = true) }
    val sortByOldest = getAllNotes.sortedBy { note -> note.lastModified }
        .filter { filterTitle -> filterTitle.title.contains(searchQuery.value, ignoreCase = true) }
    val sortByTitleAZ = getAllNotes.sortedBy { note -> note.title.lowercase() }
        .filter { filterTitle -> filterTitle.title.contains(searchQuery.value, ignoreCase = true) }
    val sortByTitleZA = getAllNotes.sortedBy { note -> note.title.lowercase() }.reversed()
        .filter { filterTitle -> filterTitle.title.contains(searchQuery.value, ignoreCase = true) }
    val sortMethod = when (selectedSortMethod.value) {
        sortOptions[0] -> sortByNewest
        sortOptions[1] -> sortByOldest
        sortOptions[2] -> sortByTitleAZ
        else -> sortByTitleZA
    }
    if (showCreateNewNoteDialog.value) {
        CreateNoteDialog(navController,
            createNewNote = { createNewNoteEvent ->
                noteViewModel.uiEvent(createNewNoteEvent)
            },
            toggleShowCreateNoteDialog = {
                noteViewModel.uiEvent(NoteUiEvent.ToggleShowCreateNewNoteDialog)
            }
        )
    }

    ExitDialog(showExitDialog)

    SortBottomSheet(showSortMethodBottomSheet, selectedSortMethod.value) {
        noteViewModel.uiEvent(NoteUiEvent.ChangeSortMethod(it))
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent(navController) {
                    scope.launch {
                        drawerState.close()
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    NoteSearchField(
                        stringResource(id = R.string.search_notes),
                        searchQuery,
                        showSortMethodBottomSheet,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 6.dp, top = 8.dp, bottom = 6.dp)
                    ) {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                },
                floatingActionButton = {
                    FloatingButton {
                        noteViewModel.uiEvent(
                            NoteUiEvent.ToggleShowCreateNewNoteDialog
                        )
                    }
                }
            ) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(it)) {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        verticalItemSpacing = 18.dp,
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxSize()
                    ) {
                        items(
                            sortMethod
                        ) { note ->
                            NoteItem(
                                noteTitle = note.title,
                                imageId = note.imageId,
                                creationDate = convertCreationDate(note.lastModified, context),
                                modifier = Modifier.fillMaxWidth(),
                                moveNoteToTrash = {
                                    noteViewModel.uiEvent(
                                        NoteUiEvent.MoveNoteToTrash(note)
                                    )
                                },
                                deleteNote = {
                                    noteViewModel.uiEvent(
                                        NoteUiEvent.DeleteNote(note)
                                    )
                                },
                                changeImage = {
                                    //change image without changing the creation/last modified date
                                    noteViewModel.uiEvent(
                                        NoteUiEvent.ChangeImage(
                                            note.copy(
                                                imageId = Random.nextInt(
                                                    animeImageList.size
                                                ), lastModified = note.lastModified
                                            )
                                        )
                                    )
                                },
                                navToEditNoteScreen = {
                                    noteViewModel.uiEvent(
                                        NoteUiEvent.NavToEditNoteScreen(
                                            navController, note.noteId
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}