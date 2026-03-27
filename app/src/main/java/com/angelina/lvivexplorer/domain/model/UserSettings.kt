package com.angelina.lvivexplorer.domain.model

data class UserSettings(
    val profileName: String = "",
    val themeMode: String = "SYSTEM",
    val onboardingDone: Boolean = false
)
