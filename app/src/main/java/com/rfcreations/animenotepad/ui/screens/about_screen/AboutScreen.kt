package com.rfcreations.animenotepad.ui.screens.about_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rfcreations.animenotepad.R
import com.rfcreations.animenotepad.ui.navigation.Screens

@Composable
fun AboutScreen(navController: NavController) {
    Scaffold(
        topBar = {
            AboutScreenTopBar {
                navController.navigate(Screens.SettingsScreen.route) {
                    popUpTo(Screens.SettingsScreen.route) {
                        inclusive = true
                    }
                }
            }
        }
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it), contentAlignment = Alignment.Center) {
            AboutScreenContent(
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

    }
}


@Composable
private fun AboutScreenContent(modifier: Modifier = Modifier) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .size(180.dp), contentAlignment = Alignment.Center
        ) {
            Icon(painterResource(id = R.drawable.ic_launcher_foreground), null)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(id = R.string.version_name))

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier,
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.thanks_for_your_support),
                style = typography.bodySmall
            )
            Text(
                text = stringResource(R.string.made_with_love),
                style = typography.bodySmall
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutScreenTopBar(onClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.about)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ), navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(Icons.Filled.ArrowBack, null)
            }
        }
    )
}