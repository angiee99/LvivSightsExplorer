package com.angelina.lvivexplorer.data.local.prefs

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.angelina.lvivexplorer.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_settings")

class UserPreferencesDataSource(private val context: Context) {
    val settingsFlow: Flow<UserSettings> = context.dataStore.data.map { prefs ->
        UserSettings(
            profileName = prefs[Keys.PROFILE_NAME] ?: "",
            themeMode = prefs[Keys.THEME_MODE] ?: "SYSTEM",
            onboardingDone = prefs[Keys.ONBOARDING_DONE] ?: false
        )
    }

    suspend fun setProfileName(name: String) {
        context.dataStore.edit { it[Keys.PROFILE_NAME] = name.trim() }
    }

    suspend fun setThemeMode(themeMode: String) {
        context.dataStore.edit { it[Keys.THEME_MODE] = themeMode }
    }

    suspend fun setOnboardingDone(done: Boolean) {
        context.dataStore.edit { it[Keys.ONBOARDING_DONE] = done }
    }

    private object Keys {
        val PROFILE_NAME: Preferences.Key<String> = stringPreferencesKey("profile_name")
        val THEME_MODE: Preferences.Key<String> = stringPreferencesKey("theme_mode")
        val ONBOARDING_DONE: Preferences.Key<Boolean> = booleanPreferencesKey("onboarding_done")
    }
}
