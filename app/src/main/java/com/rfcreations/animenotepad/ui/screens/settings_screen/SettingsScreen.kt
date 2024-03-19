package com.rfcreations.animenotepad.ui.screens.settings_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rfcreations.animenotepad.R
import com.rfcreations.animenotepad.ui.navigation.Screens
import com.rfcreations.animenotepad.ui.commons.ExitDialog
import com.rfcreations.animenotepad.utils.urlOpener

/**
 *Top level screen composable
 *Settings Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController, settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    BackHandler {
        navController.navigate(Screens.NoteScreen.route) {
            popUpTo(Screens.NoteScreen.route) { inclusive = true }
        }
    }
    val themeUiState = settingsViewModel.themeUiState
    val showExitDialog = rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val termsUrl = stringResource(id = R.string.terms_url)

    val saveNoteAutomatically = settingsViewModel.saveNoteAutomatically
    val openNoteInReadMode = settingsViewModel.openNoteInReaderMode
    val showAppThemeDialog = settingsViewModel.showAppThemeDialog.collectAsState()
    val selectedTheme = settingsViewModel.themeUiState.selectedTheme.collectAsState().value
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val theme = stringResource(id = R.string.theme)
    val noteEditing = stringResource(id = R.string.note_editing)
    val extras = stringResource(id = R.string.extra)
    ExitDialog(showExitDialog)
    if (showAppThemeDialog.value) {
        AppThemeDialog(themeUiState = themeUiState) {
            settingsViewModel.uiEvent(SettingsUiEvent.ToggleShowAppThemeDialog)
        }
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SettingsAppBar(scrollBehavior) {
                navController.navigate(Screens.NoteScreen.route) {
                    popUpTo(Screens.NoteScreen.route) {
                        inclusive = true
                    }
                }
            }
        }
    ) { paddingValue ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValue)
                .fillMaxSize()
        ) {
            item {
                Header(text = theme, modifier = Modifier.fillMaxWidth())
                AppTheme(selectedTheme) {
                    settingsViewModel.uiEvent(SettingsUiEvent.ToggleShowAppThemeDialog)
                }

                Header(text = noteEditing, modifier = Modifier.fillMaxWidth())

                SaveNoteAutomatically(
                    saveNoteAutomatically.collectAsState().value,
                    modifier = Modifier.fillMaxWidth(),
                    saveNoteChanged = { settingsViewModel.uiEvent(SettingsUiEvent.SaveNoteAutomaticallyChanged) }
                )
                OpenNotesInReaderMode(
                    openNoteInReadMode.collectAsState().value,
                    modifier = Modifier.fillMaxWidth(),
                    readModeChanged = { settingsViewModel.uiEvent(SettingsUiEvent.ReaderModeChanged) }
                )

                Header(text = extras, modifier = Modifier.fillMaxWidth())

                About(modifier = Modifier.fillMaxWidth()) {
                    navController.navigate(Screens.AboutScreen.route)
                }
                Terms(modifier = Modifier.fillMaxWidth()) {
                    urlOpener(context, termsUrl)
                }
                ContactUs(modifier = Modifier.fillMaxWidth()) {
                    navController.navigate(Screens.ContactUsScreen.route)
                }
                Exit {
                    showExitDialog.value = true
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsAppBar(scrollBehavior: TopAppBarScrollBehavior, navBackAction: () -> Unit) {
    LargeTopAppBar(
        title = { Text(text = stringResource(id = R.string.settings)) },
        navigationIcon = {
            IconButton(onClick = navBackAction) {
                Icon(Icons.Filled.ArrowBack, null)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            navigationIconContentColor = colorScheme.primary
        ),
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun Header(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier.padding(horizontal = 18.dp, vertical = 8.dp),
        style = typography.bodyMedium,
        textAlign = TextAlign.Start,
        color = colorScheme.primary
    )
}


@Composable
fun AppTheme(
    themeSelectedOption: Int,
    toggleAppThemeDialog: () -> Unit
) {
    val themeOptions = stringArrayResource(id = R.array.theme_options)
    val selectedTheme = when(themeSelectedOption){
        0 -> themeOptions[0]
        1 -> themeOptions[1]
        else -> themeOptions[2]
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                toggleAppThemeDialog()
            }
            .padding(18.dp)
    ) {
        Text(
            text = stringResource(id = R.string.app_theme),
            style = typography.titleLarge
        )
        Spacer(modifier = Modifier.height(4.dp))

        Text(text = selectedTheme, style = typography.bodyLarge)
    }

}

@Composable
private fun SaveNoteAutomatically(
    saveNoteAutomatically: Boolean,
    modifier: Modifier = Modifier,
    saveNoteChanged: () -> Unit
) {
    Row(
        modifier
            .clickable {
                saveNoteChanged()
            }
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
        Text(
            text = stringResource(R.string.save_notes_automatically),
            modifier.weight(0.8f),
            style = typography.titleLarge
        )
        Switch(
            checked = saveNoteAutomatically,
            onCheckedChange = { saveNoteChanged() }
        )
    }
}

@Composable
private fun OpenNotesInReaderMode(
    openNoteInReadMode: Boolean,
    modifier: Modifier = Modifier,
    readModeChanged: () -> Unit
) {

    Row(
        modifier
            .clickable {
                readModeChanged()
            }
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.open_notes_in_reader_mode),
            modifier.weight(0.8f),
            style = typography.titleLarge
        )
        Switch(
            checked = openNoteInReadMode,
            onCheckedChange = {
                readModeChanged()
            })
    }
}

@Composable
private fun About(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .clickable {
                onClick()
            }
            .padding(horizontal = 18.dp, vertical = 20.dp)
    ) {
        Text(text = stringResource(R.string.about), style = typography.titleLarge)
    }
}

@Composable
private fun Terms(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(horizontal = 18.dp, vertical = 20.dp)
    ) {
        Text(
            text = stringResource(R.string.terms_and_conditions),
            style = typography.titleLarge
        )
    }
}

@Composable
private fun ContactUs(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(horizontal = 18.dp, vertical = 20.dp)
    ) {
        Text(
            text = stringResource(R.string.contact_us),
            style = typography.titleLarge
        )
        Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Image(painterResource(id = R.drawable.telegram_icon), null)
            Image(painterResource(id = R.drawable.whatsapp_icon), null)
            Image(painterResource(id = R.drawable.twitter_icon), null)
            Image(painterResource(id = R.drawable.facebook_icon), null)
        }
    }
}

@Composable
private fun Exit(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(18.dp)
    ) {
        Text(text = stringResource(id = R.string.exit), style = typography.titleLarge)
        Spacer(modifier = Modifier.height(4.dp))
    }
}
