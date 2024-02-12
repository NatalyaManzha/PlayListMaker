package com.practicum.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.PlaylistMakerApp
import com.practicum.playlistmaker.ResourceProvider

class SettingsViewModel : ViewModel() {

    private var darkThemeEnabled = MutableLiveData<Boolean>()

    init {
        darkThemeEnabled.value = ResourceProvider.isAppDarkThemeEnabled()
    }

    fun observeAppTheme(): LiveData<Boolean> = darkThemeEnabled
    fun applyDarkTheme(enable: Boolean) {
        PlaylistMakerApp.switchTheme(enable)
        val saveThemeUseCase = Creator.provideSaveThemeUseCase()
        saveThemeUseCase.execute(enable)
        darkThemeEnabled.value = enable
    }

    fun shareLink() {
        Creator.provideShareLinkUseCase().execute()
    }

    fun writeToSupport() {
        Creator.provideWriteToSupportUseCase().execute()
    }

    fun goToUserAgreement() {
        Creator.provideUserAgreementUseCase().execute()
    }
}