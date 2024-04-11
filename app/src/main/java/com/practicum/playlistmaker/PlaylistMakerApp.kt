package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.interactorModule
import com.practicum.playlistmaker.di.repositoryModule
import com.practicum.playlistmaker.di.useCaseModule
import com.practicum.playlistmaker.di.viewModelModule
import com.practicum.playlistmaker.settings.domain.api.CheckoutSavedAppThemeUseCase
import com.practicum.playlistmaker.settings.domain.api.SaveThemeUseCase
import com.practicum.playlistmaker.utils.ResourceProvider
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PlaylistMakerApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PlaylistMakerApp)
            modules(dataModule, repositoryModule, useCaseModule, viewModelModule, interactorModule)
        }

        applyAppTheme()
    }

    private fun applyAppTheme() {
        val checkoutSavedAppThemeUseCase: CheckoutSavedAppThemeUseCase by inject()
        val saveThemeUseCase: SaveThemeUseCase by inject()
        val resourceProvider: ResourceProvider by inject()
        val defaultStateOfDarkTheme = resourceProvider.isAppDarkThemeEnabled()
        val darkThemeEnabled = checkoutSavedAppThemeUseCase.execute(defaultStateOfDarkTheme)
        switchTheme(darkThemeEnabled)
        saveThemeUseCase.execute(darkThemeEnabled)
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