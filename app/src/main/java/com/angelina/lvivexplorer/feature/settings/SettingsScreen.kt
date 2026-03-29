package com.angelina.lvivexplorer.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    var profileName by rememberSaveable { mutableStateOf("") }
    var isEditingProfile by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(settings.profileName) {
        if (!isEditingProfile) profileName = settings.profileName
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Settings") }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Profile name")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(if (settings.profileName.isBlank()) "Not filled" else settings.profileName)
                IconButton(
                    onClick = {
                        if (isEditingProfile) {
                            viewModel.saveProfileName(profileName)
                            isEditingProfile = false
                        } else {
                            profileName = settings.profileName
                            isEditingProfile = true
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit profile name"
                    )
                }
            }
            if (isEditingProfile) {
                OutlinedTextField(
                    value = profileName,
                    onValueChange = { profileName = it },
                    label = { Text("Profile name (local)") },
                    placeholder = { Text("Not filled") },
                    modifier = Modifier.fillMaxWidth()
                )
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
