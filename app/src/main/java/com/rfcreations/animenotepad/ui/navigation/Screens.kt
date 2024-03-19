package com.rfcreations.animenotepad.ui.navigation

sealed class Screens(val route: String){
    data object NoteScreen: Screens("note_screen")
    data object EditNoteScreen: Screens("edit_note_screen")
    data object SettingsScreen: Screens("settings_screen")
    data object TrashScreen: Screens("trash_screen")
    data object ContactUsScreen: Screens("contact_us_screen")
    data object AboutScreen: Screens("about_screen")
}
