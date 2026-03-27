package com.angelina.lvivexplorer.data.repository

import com.angelina.lvivexplorer.data.local.prefs.UserPreferencesDataSource
import com.angelina.lvivexplorer.domain.model.UserSettings
import com.angelina.lvivexplorer.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource
) : SettingsRepository {
    override fun observeSettings(): Flow<UserSettings> = userPreferencesDataSource.settingsFlow

    override suspend fun setProfileName(name: String) {
        userPreferencesDataSource.setProfileName(name)
    }

    override suspend fun setThemeMode(mode: String) {
        userPreferencesDataSource.setThemeMode(mode)
    }
}
