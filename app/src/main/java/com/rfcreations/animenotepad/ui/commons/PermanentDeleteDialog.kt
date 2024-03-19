package com.rfcreations.animenotepad.ui.commons

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import com.rfcreations.animenotepad.R

@Composable
fun PermanentDeleteDialog(
    showPermanentDeleteDialog: MutableState<Boolean>,
    deleteNote: () -> Unit
) {
    if (showPermanentDeleteDialog.value) {
        AlertDialog(
            onDismissRequest = { showPermanentDeleteDialog.value = false },
            confirmButton = {
                TextButton(
                    onClick = deleteNote,
                    colors = ButtonDefaults.textButtonColors(contentColor = colorScheme.error)
                ) {
                    Text(text = stringResource(id = R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermanentDeleteDialog.value = false }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            },
            title = {
                Text(text = stringResource(id = R.string.delete_note_title))
            },
            text = {
                Text(text = stringResource(id = R.string.permanent_delete_content))

            }
        )
    }
}