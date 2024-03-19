package com.rfcreations.animenotepad.ui.screens.note_screen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController
import com.rfcreations.animenotepad.R
import com.rfcreations.animenotepad.ui.screens.note_screen.NoteUiEvent

@Composable
fun CreateNoteDialog(
    navController: NavController,
    createNewNote: (NoteUiEvent.CreateNewNote) -> Unit,
    toggleShowCreateNoteDialog: () -> Unit
) {
    val noteTitle = rememberSaveable { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = { toggleShowCreateNoteDialog() },
        confirmButton = {
            ProceedButton(noteTitle.value) {
                createNewNote(NoteUiEvent.CreateNewNote(noteTitle.value,navController))
            }
        },
        title = {
            Text(text = stringResource(id = R.string.enter_title))
        },
        text = {
            TitleTextField(noteTitle)
        }
    )
}

@Composable
fun ProceedButton(noteTitle: String, createNewNote: () -> Unit) {
    Button(
        onClick = {
            createNewNote()
        },
        enabled = noteTitle.isNotEmpty(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = stringResource(R.string.proceed))
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TitleTextField(noteTitle: MutableState<String>) {
    val localKeyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = noteTitle.value,
        label = { Text(text = stringResource(R.string.title)) },
        modifier = Modifier.fillMaxWidth(),
        shape = shapes.large,
        singleLine = true,
        onValueChange = { if (it.length < 40) noteTitle.value = it },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions {
            localKeyboardController?.hide()
        }
    )
}