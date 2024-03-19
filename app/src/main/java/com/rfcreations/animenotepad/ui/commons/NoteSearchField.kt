package com.rfcreations.animenotepad.ui.commons

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import com.rfcreations.animenotepad.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NoteSearchField(
    placeHolder: String,
    searchQuery: MutableState<String>,
    showSortMethodBottomSheet: MutableState<Boolean>?,
    modifier: Modifier = Modifier,
    openNavigationDrawer: () -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    Row(verticalAlignment = Alignment.CenterVertically) {

        OutlinedTextField(
            modifier = modifier.weight(0.8f),
            value = searchQuery.value,
            onValueChange = { if (it.length < 10) searchQuery.value = it },
            leadingIcon = {
                IconButton(onClick = openNavigationDrawer) {
                    Icon(Icons.Filled.Menu, null)
                }
            },
            trailingIcon = {
                if (searchQuery.value.isNotEmpty()) {
                    IconButton(onClick = { searchQuery.value = ""; keyboardController?.hide() }) {
                        Icon(Icons.Filled.Clear, null)
                    }
                }
            },
            maxLines = 1,
            placeholder = {
                Text(text = placeHolder)
            },
            shape = shapes.extraLarge,
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions {
                keyboardController?.hide()
            }
        )
        if (showSortMethodBottomSheet?.value != null) {
            IconButton(
                onClick = {
                    showSortMethodBottomSheet.value = !showSortMethodBottomSheet.value
                }, modifier = modifier.weight(0.2f)
            ) {
                Icon(painterResource(id = R.drawable.sort_method_icon), null)
            }
        }
    }
}