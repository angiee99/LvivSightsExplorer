package com.angelina.lvivexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.angelina.lvivexplorer.feature.settings.SettingsViewModel
import com.angelina.lvivexplorer.navigation.LvivExplorerApp
import com.angelina.lvivexplorer.ui.theme.LvivArchitectureExplorerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val settings = settingsViewModel.settings.collectAsStateWithLifecycle().value
            val useDarkTheme = when (settings.themeMode) {
                "DARK" -> true
                "LIGHT" -> false
                else -> isSystemInDarkTheme()
            }
            LvivArchitectureExplorerTheme(darkTheme = useDarkTheme) {
                LvivExplorerApp()
            }
        }
    }
}
