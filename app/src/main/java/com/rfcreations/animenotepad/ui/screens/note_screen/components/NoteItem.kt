package com.rfcreations.animenotepad.ui.screens.note_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rfcreations.animenotepad.R
import com.rfcreations.animenotepad.ui.commons.PermanentDeleteDialog
import com.rfcreations.animenotepad.utils.animeImageList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun NoteItem(
    noteTitle: String,
    imageId: Int,
    creationDate: String,
    modifier: Modifier = Modifier,
    moveNoteToTrash: () -> Unit,
    deleteNote: () -> Unit,
    changeImage: () -> Unit,
    navToEditNoteScreen: () -> Unit
) {

    val expandNoteItemDropDownMenu = rememberSaveable { mutableStateOf(false) }
    val showPermanentDeleteDialog = rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = modifier
            .clickable {
                navToEditNoteScreen()
            },
        contentAlignment = Alignment.Center
    ) {
        Column(modifier.padding(10.dp)) {
            AnimeImage(imageId, modifier)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                NoteContent(noteTitle, creationDate, modifier.weight(0.7f))
                IconButton(
                    modifier = modifier.weight(0.3f),
                    onClick = {
                        expandNoteItemDropDownMenu.value = true
                    }
                ) {
                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
                }
            }
        }
        NoteItemDropDownMenu(
            expandNoteItemDropDownMenu,
            moveNoteToTrash,
            changeImage,
            showPermDeleteDialog = {
                showPermanentDeleteDialog.value = !showPermanentDeleteDialog.value
            }
        )

    }

    PermanentDeleteDialog(showPermanentDeleteDialog) {
        CoroutineScope(Dispatchers.IO).launch {
            deleteNote()
            showPermanentDeleteDialog.value = false
        }
    }
}

@Composable
private fun AnimeImage(imageId: Int, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(4.dp)
    ) {
        Image(
            painter = painterResource(animeImageList[imageId].id),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(120.dp)
                .clip(RoundedCornerShape(8.dp))
        )
    }
}

@Composable
private fun NoteContent(noteTitle: String, creationDate: String, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(
            text = noteTitle,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = creationDate,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun NoteItemDropDownMenu(
    showDropDown: MutableState<Boolean>,
    moveNoteToTrash: () -> Unit,
    changeImage: () -> Unit,
    showPermDeleteDialog: () -> Unit,
) {
    DropdownMenu(
        expanded = showDropDown.value,
        onDismissRequest = { showDropDown.value = false }
    ) {

        DropdownMenuItem(
            text = {
                Text(text = stringResource(R.string.change_image))
            },
            onClick = { changeImage(); showDropDown.value = false }
        )
        DropdownMenuItem(
            text = {
                Text(text = stringResource(R.string.move_to_trash))
            },
            onClick = { moveNoteToTrash(); showDropDown.value = false }
        )
        DropdownMenuItem(
            text = {
                Text(text = stringResource(R.string.delete))
            },
            onClick = { showPermDeleteDialog(); showDropDown.value = false }
        )

    }
}