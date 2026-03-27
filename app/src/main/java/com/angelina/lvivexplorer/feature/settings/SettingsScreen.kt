package com.angelina.lvivexplorer.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    var profileName by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(settings.profileName) {
        if (profileName.isBlank()) profileName = settings.profileName
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Settings") }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = profileName,
                onValueChange = { profileName = it },
                label = { Text("Profile name (local)") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { viewModel.saveProfileName(profileName) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save profile")
            }
            Text("Theme mode")
            Button(
                onClick = { viewModel.saveThemeMode("LIGHT") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Light")
            }
            Button(
                onClick = { viewModel.saveThemeMode("DARK") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Dark")
            }
            Button(
                onClick = { viewModel.saveThemeMode("SYSTEM") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("System")
            }
            Text("Current theme: ${settings.themeMode}")
        }
    }
}
