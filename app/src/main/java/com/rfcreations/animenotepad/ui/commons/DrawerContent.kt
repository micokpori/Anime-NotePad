package com.rfcreations.animenotepad.ui.commons

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rfcreations.animenotepad.R
import com.rfcreations.animenotepad.ui.navigation.Screens

@Composable
fun DrawerContent(navController: NavController, closeDrawer: () -> Unit) {
    val drawerImage = painterResource(id = R.drawable.anime_girl14)
    val backStackEntry = navController.currentBackStackEntryAsState()
    ModalDrawerSheet {
        CircularAnimeImage(
            image = drawerImage, modifier = Modifier
                .padding(top = 30.dp)
                .size(120.dp)
                .align(CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(30.dp))
        NavigationDrawerItem(
            label = { Text(text = stringResource(id = R.string.notes)) },
            selected = Screens.NoteScreen.route == backStackEntry.value?.destination?.route,
            onClick = {
                if (Screens.NoteScreen.route != backStackEntry.value?.destination?.route) {
                    closeDrawer()
                    navController.navigate(Screens.NoteScreen.route)
                } else {
                    closeDrawer()
                }
            },
            icon = {
                if (Screens.NoteScreen.route == backStackEntry.value?.destination?.route) {
                    Icon(painterResource(id = R.drawable.filled_notes_icon), null)
                } else {
                    Icon(painterResource(id = R.drawable.outlined_notes_icon), null)
                }
            }
        )
        NavigationDrawerItem(
            label = { Text(text = stringResource(id = R.string.trash)) },
            selected = Screens.TrashScreen.route == backStackEntry.value?.destination?.route,
            onClick = {
                if (Screens.TrashScreen.route != backStackEntry.value?.destination?.route) {
                    closeDrawer()
                    navController.navigate(Screens.TrashScreen.route)
                } else {
                    closeDrawer()
                }
            },
            icon = {
                if (Screens.TrashScreen.route == backStackEntry.value?.destination?.route) {
                    Icon(Icons.Filled.Delete, null)
                } else {
                    Icon(Icons.Outlined.Delete, null)
                }
            }
        )
        NavigationDrawerItem(
            label = {
                Text(text = stringResource(id = R.string.settings))
            },
            selected = Screens.SettingsScreen.route == backStackEntry.value?.destination?.route,
            onClick = {
                closeDrawer()
                navController.navigate(Screens.SettingsScreen.route)
            },
            icon = {
                Icon(Icons.Outlined.Settings, null)
            }
        )
    }

}