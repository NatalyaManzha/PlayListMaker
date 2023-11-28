package com.practicum.playlistmaker
import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

const val PREFERENCES = "preferences"
const val NIGHT_THEME_ENABLED = "night_theme_enabled"
const val SEARCH_HISTORY_UPDATE = "search_history_update"

class PlaylistMakerApp : Application() {
    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        checkoutTheme()
        switchTheme(darkTheme)
    }

    private fun checkoutTheme() {
        var defaultStateOfDarkTheme = "false"
        val currentDeviceNightMode =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentDeviceNightMode) {
            Configuration.UI_MODE_NIGHT_YES -> {
                defaultStateOfDarkTheme = "true"
            }

            Configuration.UI_MODE_NIGHT_NO -> {
                defaultStateOfDarkTheme = "false"
            }
        }
        val sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPrefs.getString(NIGHT_THEME_ENABLED, defaultStateOfDarkTheme).toBoolean()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}