package com.rfcreations.animenotepad.ui.screens.settings_screen

sealed class SettingsUiEvent {
    data object ReaderModeChanged: SettingsUiEvent()
    data object SaveNoteAutomaticallyChanged : SettingsUiEvent()
    data object ToggleShowAppThemeDialog : SettingsUiEvent()
}