package com.rfcreations.animenotepad.ui.screens.edit_note_screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rfcreations.animenotepad.R
import com.rfcreations.animenotepad.database.note.NoteEntity
import com.rfcreations.animenotepad.ui.navigation.Screens

/**
 *Top Level Screen Composable
 *This screen composable creates and edit notes
 */
@Composable
fun EditNoteScreen(
    navController: NavController,
    noteId: String,
) {
    val editNoteViewModel = hiltViewModel<EditNoteViewModel>()
    val getNote = editNoteViewModel.getNoteById(noteId)
    val saveNoteAutomaticallyState =
        editNoteViewModel.saveNoteAutomaticallyPref
    val openNoteInReadMode = editNoteViewModel.openNoteInReadMode
    val context = LocalContext.current
    BackHandler {
        navController.navigate(Screens.NoteScreen.route) {
            popUpTo(Screens.NoteScreen.route) {
                inclusive = true
            }
        }
    }
    val noteTitle = rememberSaveable {
        mutableStateOf(
            getNote.title
        )
    }
    val noteContent = rememberSaveable {
        mutableStateOf(
            getNote.content
        )
    }
    val imageId = remember { getNote.imageId }
    val expandTopBarMenu = rememberSaveable { mutableStateOf(false) }
    val readerModeEnabled = rememberSaveable {
        mutableStateOf(openNoteInReadMode)
    }
    val showRenameNoteDialog = rememberSaveable { mutableStateOf(false) }
    RenameNoteTitleDialog(showRenameNoteDialog, noteTitle.value) { newTitle ->
        editNoteViewModel.uiEvent(
            EditNoteUiEvent.RenameNote(
                NoteEntity(
                    noteId = noteId,
                    imageId = imageId,
                    title = newTitle,
                    content = noteContent.value
                )
            )
        )
        noteTitle.value = newTitle
    }

    Scaffold(
        topBar = {
            EditNoteAppBar(
                noteTitle = noteTitle.value,
                expandTopBarMenu = expandTopBarMenu,
                readerModeEnabled = readerModeEnabled,
                navBackAction = {
                    navController.navigate(Screens.NoteScreen.route) {
                        popUpTo(Screens.NoteScreen.route) {
                            inclusive = true
                        }
                    }
                },
                showRenameNoteDialogAction = {
                    showRenameNoteDialog.value = !showRenameNoteDialog.value
                },
                saveNote = {
                    editNoteViewModel.uiEvent(
                        EditNoteUiEvent.SaveNote(
                            NoteEntity(
                                noteId = noteId,
                                imageId = imageId,
                                title = noteTitle.value,
                                content = noteContent.value
                            )
                        )
                    )
                },
                shareNoteText = {
                    editNoteViewModel.uiEvent(
                        EditNoteUiEvent.ShareNoteAsText(
                            noteTitle.value,
                            noteContent.value,
                            context
                        )
                    )
                }, shareNoteFile = {
                    editNoteViewModel.uiEvent(
                        EditNoteUiEvent.ShareNoteAsFile(
                            noteTitle.value,
                            noteContent.value,
                            context
                        )
                    )
                }
            )
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        ) {
            NoteContent(
                noteContent,
                readerModeEnabled,
                insertNote = {
                    if (saveNoteAutomaticallyState) {
                        editNoteViewModel.uiEvent(
                            EditNoteUiEvent.SaveNote(
                                NoteEntity(
                                    noteId = noteId,
                                    imageId = imageId,
                                    title = noteTitle.value,
                                    content = noteContent.value
                                )
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)

            )
        }
    }
}


@Composable
private fun NoteContent(
    content: MutableState<String>,
    readerModeEnabled: MutableState<Boolean>,
    insertNote: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = content.value,
        onValueChange = { content.value = it; insertNote() },
        modifier,
        readOnly = readerModeEnabled.value,
        textStyle = typography.bodyLarge,
        placeholder = {
            Text(text = stringResource(id = R.string.enter_note))
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditNoteAppBar(
    noteTitle: String,
    navBackAction: () -> Unit,
    expandTopBarMenu: MutableState<Boolean>,
    readerModeEnabled: MutableState<Boolean>,
    showRenameNoteDialogAction: () -> Unit,
    saveNote: () -> Unit,
    shareNoteText: () -> Unit,
    shareNoteFile: () -> Unit,
) {
    TopAppBar(
        title = {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = noteTitle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = { navBackAction() },
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            TextButton(
                onClick = { saveNote() },
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.save)
                )
            }

            IconButton(
                onClick = {
                    Log.d("oldValue", "${expandTopBarMenu.value}")
                    expandTopBarMenu.value = !expandTopBarMenu.value
                    Log.d("newValue", "${expandTopBarMenu.value}")
                }
            ) {
                Icon(Icons.Filled.MoreVert, contentDescription = null)
            }
            AppBarDropDownMenu(
                expandTopBarMenu, readerModeEnabled,
                showRenameNoteDialogAction = showRenameNoteDialogAction,
                shareNoteText,
                shareNoteFile
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            navigationIconContentColor = colorScheme.primary
        )

    )
}

@Composable
private fun AppBarDropDownMenu(
    expandTopBarMenu: MutableState<Boolean>,
    readerModeEnabled: MutableState<Boolean>,
    showRenameNoteDialogAction: () -> Unit,
    shareNoteText: () -> Unit,
    shareNoteFile: () -> Unit,

    ) {
    DropdownMenu(
        expanded = expandTopBarMenu.value,
        onDismissRequest = { expandTopBarMenu.value = false }
    ) {
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.rename)) },
            onClick = {
                showRenameNoteDialogAction()
                expandTopBarMenu.value = false
            },
            leadingIcon = {
                Icon(painterResource(id = R.drawable.rename_icon), null)
            }
        )

        DropdownMenuItem(
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.reader_mode))
                    Checkbox(
                        checked = readerModeEnabled.value,
                        onCheckedChange = {
                            readerModeEnabled.value = it
                            expandTopBarMenu.value = !expandTopBarMenu.value
                        }
                    )

                }
            },
            onClick = {
                readerModeEnabled.value = !readerModeEnabled.value
                expandTopBarMenu.value = !expandTopBarMenu.value
            },
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.reader_mode_icon),
                    contentDescription = null
                )
            }
        )
        DropdownMenuItem(
            text = {
                Text(text = stringResource(R.string.share_note))
            },
            leadingIcon = {
                Icon(Icons.Filled.Share, null)
            },
            onClick = { shareNoteText(); expandTopBarMenu.value = false }
        )
        DropdownMenuItem(
            text = {
                Text(text = stringResource(R.string.share_note_as_file))
            },
            leadingIcon = {
                Icon(Icons.Filled.Share, null)
            },
            onClick = { shareNoteFile(); expandTopBarMenu.value = false }
        )
    }
}
