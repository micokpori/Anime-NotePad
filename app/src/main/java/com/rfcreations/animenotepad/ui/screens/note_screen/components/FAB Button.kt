package com.rfcreations.animenotepad.ui.screens.note_screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FloatingButton(showCreateNewNoteDialog: () -> Unit) {
    FloatingActionButton(
        onClick = { showCreateNewNoteDialog() },
        modifier = Modifier
            .padding(12.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {

            Icon(Icons.Filled.Add, contentDescription = null)

        }

    }
}