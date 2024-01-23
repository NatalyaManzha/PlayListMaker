package com.practicum.playlistmaker.presentation.presenter

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.domain.NIGHT_THEME_ENABLED
import com.practicum.playlistmaker.domain.PREFERENCES

class PlaylistMakerApp : Application() {
    private var darkTheme = false
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        checkoutTheme()
        switchTheme(darkTheme)
    }

    private fun checkoutTheme() {
        var defaultStateOfDarkTheme =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        darkTheme = sharedPrefs.getBoolean(NIGHT_THEME_ENABLED, defaultStateOfDarkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPrefs.edit()
            .putBoolean(NIGHT_THEME_ENABLED, darkThemeEnabled)
            .apply()
    }

}