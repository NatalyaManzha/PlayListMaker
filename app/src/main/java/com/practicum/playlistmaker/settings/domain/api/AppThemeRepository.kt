package com.practicum.playlistmaker.settings.domain.api

interface AppThemeRepository {

    fun checkoutSavedTheme(defaultStateOfDarkTheme: Boolean): Boolean
    fun saveTheme(darkThemeEnabled: Boolean)
}