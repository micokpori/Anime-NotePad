package com.rfcreations.animenotepad.ui.screens.settings_screen

import androidx.lifecycle.ViewModel
import com.rfcreations.animenotepad.repository.UserPreferenceRepository
import com.rfcreations.animenotepad.utils.ThemeUiState
import com.rfcreations.animenotepad.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository,
    val themeUiState: ThemeUiState
) : ViewModel() {

    private val prefKeys = Constants.PrefKeys

    private val _saveNoteAutomatically = MutableStateFlow(
        userPreferenceRepository.getBooleanPref(
            prefKeys.SAVE_NOTE_AUTOMATICALLY, true
        )
    )
    val saveNoteAutomatically = _saveNoteAutomatically.asStateFlow()

    private val _openNoteInReaderMode = MutableStateFlow(
        userPreferenceRepository.getBooleanPref(
            prefKeys.OPEN_NOTE_IN_READER_MODE, false
        )
    )
    val openNoteInReaderMode = _openNoteInReaderMode.asStateFlow()

    private val _showAppThemeDialog = MutableStateFlow(false)
    val showAppThemeDialog = _showAppThemeDialog.asStateFlow()

    fun uiEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.SaveNoteAutomaticallyChanged -> {
                _saveNoteAutomatically.value = !_saveNoteAutomatically.value
                userPreferenceRepository.editBooleanPref(
                    prefKeys.SAVE_NOTE_AUTOMATICALLY,
                    _saveNoteAutomatically.value
                )
            }
            is SettingsUiEvent.ReaderModeChanged -> {
                _openNoteInReaderMode.value = !_openNoteInReaderMode.value
                userPreferenceRepository.editBooleanPref(
                    prefKeys.OPEN_NOTE_IN_READER_MODE,
                    _openNoteInReaderMode.value
                )
            }
            is SettingsUiEvent.ToggleShowAppThemeDialog -> {
                _showAppThemeDialog.value = !_showAppThemeDialog.value
            }
        }
    }
}