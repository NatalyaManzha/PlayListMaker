package com.practicum.playlistmaker.data.impl

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.domain.PREFERENCES
import com.practicum.playlistmaker.domain.api.AppThemeRepository

class AppThemeRepositoryImpl(context: Context) : AppThemeRepository {
    private val sharedPreferences =
        context.getSharedPreferences(PREFERENCES, AppCompatActivity.MODE_PRIVATE)

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