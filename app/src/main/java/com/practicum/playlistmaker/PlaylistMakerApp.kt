package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.utils.Creator
import com.practicum.playlistmaker.utils.ResourceProvider

class PlaylistMakerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Creator.setApplication(this)
        ResourceProvider.setApplication(this)
        applyAppTheme()
    }

    private fun applyAppTheme() {
        val checkoutSavedAppThemeUseCase = Creator.provideCheckoutSavedAppThemeUseCase()
        val saveThemeUseCase = Creator.provideSaveThemeUseCase()
        val defaultStateOfDarkTheme = ResourceProvider.isAppDarkThemeEnabled()
        val darkTheme = checkoutSavedAppThemeUseCase.execute(defaultStateOfDarkTheme)
        switchTheme(darkTheme)
        saveThemeUseCase.execute(darkTheme)
    }

    companion object {
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
}