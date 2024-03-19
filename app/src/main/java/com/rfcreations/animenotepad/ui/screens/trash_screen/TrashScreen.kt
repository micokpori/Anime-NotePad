package com.rfcreations.animenotepad.ui.screens.trash_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rfcreations.animenotepad.R
import com.rfcreations.animenotepad.ui.commons.DrawerContent
import com.rfcreations.animenotepad.ui.commons.NoteSearchField
import com.rfcreations.animenotepad.utils.convertCreationDate
import kotlinx.coroutines.launch

/**
 *Top level composable
 *Entry trash screen
 */
@Composable
fun TrashScreen(navController: NavController, trashViewModel: TrashViewModel = hiltViewModel()) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val getNotes = trashViewModel.getAllTrashedNotes().collectAsState(initial = emptyList())
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    Surface(modifier = Modifier.fillMaxSize()) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent(navController) {
                    scope.launch {
                        drawerState.apply {
                            close()
                        }
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    NoteSearchField(
                        stringResource(id = R.string.search_trash),
                        searchQuery, null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                }
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .padding(6.dp)
                ) {
                    items(
                        getNotes.value.sortedBy { trashItem ->
                            trashItem.lastModified
                        }.reversed().filter { filterTitle ->
                            filterTitle.title.contains(searchQuery.value, ignoreCase = true)
                        }
                    ) { trashItem ->

                        TrashItem(
                            trashTitle = trashItem.title,
                            trashImageId = trashItem.imageId,
                            creationDate = convertCreationDate(trashItem.lastModified, context),
                            modifier = Modifier.fillMaxWidth(),
                            restoreNoteItem = {
                                trashViewModel.uiEvent(
                                    TrashUiEvent.RestoreNote(trashItem)
                                )
                            },
                            permanentDeleteNoteAction = {
                                trashViewModel.uiEvent(
                                    TrashUiEvent.PermanentDeleteTrashNote(
                                        trashItem
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