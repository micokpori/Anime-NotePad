package com.rfcreations.animenotepad.ui.screens.edit_note_screen


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
import com.rfcreations.animenotepad.R

@Composable
fun RenameNoteTitleDialog(
    showDialog: MutableState<Boolean>,
    noteToRename: String,
    renameAction: (String) -> Unit
) {
    if (showDialog.value) {
        val newNoteTitle = rememberSaveable { mutableStateOf(noteToRename) }
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                RenameButton(newNoteTitle.value) { renameAction(newNoteTitle.value); showDialog.value = false }
            },
            title = {
                Text(text = stringResource(id = R.string.rename))
            },
            text = {
                RenameTextField(newNoteTitle)
            }
        )
    }
}

@Composable
private fun RenameButton(newNoteTitle: String, renameAction: () -> Unit) {
    Button(
        onClick = { renameAction() },
        enabled = newNoteTitle.isNotEmpty(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = stringResource(R.string.rename))
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun RenameTextField(noteToRename: MutableState<String>) {
    val localKeyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = noteToRename.value,
        modifier = Modifier.fillMaxWidth(),
        shape = shapes.large,
        singleLine = true,
        onValueChange = { if (it.length < 40) noteToRename.value = it },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions {
            localKeyboardController?.hide()
        }
    )
}