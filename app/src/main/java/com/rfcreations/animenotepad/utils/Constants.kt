package com.rfcreations.animenotepad.utils

object Constants {
    const val DATABASE_NAME = "NoteDatabase.db"
    object PrefKeys{
        const val PREF_NAME = "MyAppPref"
        const val SELECTED_THEME_KEY = "SelectedTheme"
        const val DYNAMIC_THEME_KEY = "DynamicThemeEnabled"
        const val SORT_METHOD_KEY = "SelectedSortMethod"
        const val SAVE_NOTE_AUTOMATICALLY = "SaveNoteAutomatically"
        const val OPEN_NOTE_IN_READER_MODE = "OpenNoteInReaderMode"
    }

    object NavArgKeys{
        const val NOTE_ID = "noteId"
    }
}