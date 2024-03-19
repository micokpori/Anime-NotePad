package com.rfcreations.animenotepad.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rfcreations.animenotepad.ui.screens.about_screen.AboutScreen
import com.rfcreations.animenotepad.ui.screens.contact_us_screen.ContactUsScreen
import com.rfcreations.animenotepad.ui.screens.edit_note_screen.EditNoteScreen
import com.rfcreations.animenotepad.ui.screens.note_screen.NoteViewModel
import com.rfcreations.animenotepad.ui.screens.note_screen.NotesScreen
import com.rfcreations.animenotepad.ui.screens.settings_screen.SettingsScreen
import com.rfcreations.animenotepad.ui.screens.trash_screen.TrashScreen
import com.rfcreations.animenotepad.utils.Constants

@Composable
fun NavigationSetup() {

    val navController = rememberNavController()
    val noteViewModel: NoteViewModel = hiltViewModel()
    NavHost(navController, Screens.NoteScreen.route) {
        composable(Screens.NoteScreen.route) {
            NotesScreen(navController,noteViewModel)
        }
        composable(Screens.TrashScreen.route) {
            TrashScreen(navController)
        }
        composable(Screens.SettingsScreen.route) {
            SettingsScreen(navController)
        }
        composable(
            route = Screens.EditNoteScreen.route +
                    "/{${Constants.NavArgKeys.NOTE_ID}}",
            arguments = listOf(
                navArgument(Constants.NavArgKeys.NOTE_ID) {
                    type = NavType.StringType
                }
            )
        ) { entry ->
            EditNoteScreen(
                navController = navController,
                noteId = entry.arguments?.getString(Constants.NavArgKeys.NOTE_ID) ?: ""
            )
        }

        composable(Screens.ContactUsScreen.route){
            ContactUsScreen(navController)
        }

        composable(Screens.AboutScreen.route){
            AboutScreen(navController)
        }
    }
}