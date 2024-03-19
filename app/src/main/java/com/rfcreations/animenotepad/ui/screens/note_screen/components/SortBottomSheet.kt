package com.rfcreations.animenotepad.ui.screens.note_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rfcreations.animenotepad.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    showSortMethodBottomSheet: MutableState<Boolean>,
    selectedSortMethod: String?,
    changeSortMethod: (String) -> Unit
) {
    val context = LocalContext.current
    val windowInsets = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    if (showSortMethodBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showSortMethodBottomSheet.value = false },
            modifier = Modifier.padding(windowInsets)
        ) {
            Title(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .align(Alignment.Start)
            )
            SortItem(
                selectedSortMethod ?: context.getString(R.string.default_sort_method),
                modifier = Modifier.fillMaxWidth()
            ) {
                changeSortMethod(it)
                showSortMethodBottomSheet.value = false
            }

        }
    }
}

@Composable
private fun Title(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = R.string.sort_by),
        style = typography.bodySmall,
        modifier = modifier,
        color = colorScheme.tertiary
    )
}

@Composable
private fun SortItem(
    selectedSortMethod: String,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    val sortOptions = stringArrayResource(id = R.array.sort_options)
    sortOptions.forEach { sortMethod ->
        Row(
            modifier = modifier
                .clickable {
                    onClick(sortMethod)
                }
                .padding(horizontal = 12.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,

            ) {
            Text(text = sortMethod, modifier.weight(0.8f))
            if (selectedSortMethod == sortMethod) {
                Icon(
                    modifier = modifier.weight(0.2f),
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = colorScheme.primary
                )
            }
        }
    }
}