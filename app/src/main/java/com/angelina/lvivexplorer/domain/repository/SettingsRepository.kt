package com.angelina.lvivexplorer.domain.repository

import com.angelina.lvivexplorer.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observeSettings(): Flow<UserSettings>
    suspend fun setProfileName(name: String)
    suspend fun setThemeMode(mode: String)
}
