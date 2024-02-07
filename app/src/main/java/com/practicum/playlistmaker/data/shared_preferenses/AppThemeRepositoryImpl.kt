package com.practicum.playlistmaker.data.shared_preferenses

import android.content.SharedPreferences
import com.practicum.playlistmaker.domain.api.AppThemeRepository

class AppThemeRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    AppThemeRepository {


    override fun checkoutSavedTheme(defaultStateOfDarkTheme: Boolean): Boolean {
        return sharedPreferences.getBoolean(NIGHT_THEME_ENABLED, defaultStateOfDarkTheme)
    }

    override fun saveTheme(darkThemeEnabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean(NIGHT_THEME_ENABLED, darkThemeEnabled)
            .apply()
    }

    companion object {
        const val NIGHT_THEME_ENABLED = "night_theme_enabled"
    }
}