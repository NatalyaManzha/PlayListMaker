package com.practicum.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.Creator

class PlaylistMakerApp : Application() {
    private var darkTheme = false
    private val checkoutSavedAppThemeUseCase by lazy { Creator.provideCheckoutSavedAppThemeUseCase() }
    private val saveThemeUseCase by lazy { Creator.provideSaveThemeUseCase() }

    override fun onCreate() {
        super.onCreate()
        Creator.setApplication(this)
        var defaultStateOfDarkTheme =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        darkTheme = checkoutSavedAppThemeUseCase.execute(defaultStateOfDarkTheme)
        switchTheme(darkTheme)
        saveThemeUseCase.execute(darkTheme)
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