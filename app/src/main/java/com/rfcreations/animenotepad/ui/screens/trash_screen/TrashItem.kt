package com.rfcreations.animenotepad.ui.screens.trash_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rfcreations.animenotepad.R
import com.rfcreations.animenotepad.ui.commons.PermanentDeleteDialog
import com.rfcreations.animenotepad.utils.animeImageList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TrashItem(
    trashTitle: String,
    trashImageId: Int,
    creationDate: String,
    modifier: Modifier = Modifier,
    restoreNoteItem: () -> Unit,
    permanentDeleteNoteAction: () -> Unit
) {
    val showPermanentDeleteDialog = rememberSaveable { mutableStateOf(false) }

    ElevatedCard(modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AnimeImage(trashImageId = trashImageId, modifier = Modifier.size(120.dp))
            Spacer(modifier = Modifier.width(20.dp))
            TrashContent(trashTitle, creationDate, modifier.weight(0.4f))
            DeleteIcon(
                modifier = modifier.weight(0.15f),
                showPermanentDeleteDialog = showPermanentDeleteDialog
            )
            UndoIcon(modifier = modifier.weight(0.15f), undoAction = restoreNoteItem)
        }

    }
    PermanentDeleteDialog(showPermanentDeleteDialog) {
        CoroutineScope(Dispatchers.IO).launch {
            permanentDeleteNoteAction()
            showPermanentDeleteDialog.value = false
        }
    }

}

@Composable
private fun AnimeImage(trashImageId: Int, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(animeImageList[trashImageId].id),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(120.dp)
            .clip(CircleShape)
    )
}

@Composable
private fun TrashContent(
    trashTitle: String,
    creationDate: String,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = trashTitle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = creationDate,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun DeleteIcon(
    modifier: Modifier = Modifier,
    showPermanentDeleteDialog: MutableState<Boolean>
) {
    IconButton(
        modifier = modifier,
        onClick = { showPermanentDeleteDialog.value = true }) {
        Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
    }
}

@Composable
private fun UndoIcon(
    modifier: Modifier = Modifier,
    undoAction: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = undoAction
    ) {
        Icon(painterResource(R.drawable.restore_note_icon), contentDescription = null)
    }
}