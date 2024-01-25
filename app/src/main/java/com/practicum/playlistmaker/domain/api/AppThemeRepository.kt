package com.practicum.playlistmaker.domain.api

interface AppThemeRepository {

    fun checkoutSavedTheme(defaultStateOfDarkTheme: Boolean): Boolean
    fun saveTheme(darkThemeEnabled: Boolean)
}