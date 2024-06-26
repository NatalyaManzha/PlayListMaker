package com.practicum.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.PlaylistMakerApp
import com.practicum.playlistmaker.settings.domain.api.CheckoutSavedAppThemeUseCase
import com.practicum.playlistmaker.settings.domain.api.SaveThemeUseCase
import com.practicum.playlistmaker.settings.ui.models.SettingsUiEvent
import com.practicum.playlistmaker.sharing.domain.api.ShareLinkUseCase
import com.practicum.playlistmaker.sharing.domain.api.UserAgreementUseCase
import com.practicum.playlistmaker.sharing.domain.api.WriteToSupportUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow

class SettingsViewModel(
    private val saveThemeUseCase: SaveThemeUseCase,
    private val shareLinkUseCase: ShareLinkUseCase,
    private val writeToSupportUseCase: WriteToSupportUseCase,
    private val userAgreementUseCase: UserAgreementUseCase,
    checkoutSavedAppThemeUseCase: CheckoutSavedAppThemeUseCase
) : ViewModel() {

    private val _darkThemeEnabled = MutableStateFlow<Boolean>(false)
    val darkThemeEnabledFlow = _darkThemeEnabled.asSharedFlow()


    init {
        _darkThemeEnabled.value =
            checkoutSavedAppThemeUseCase.execute(defaultStateOfDarkTheme = false)
    }


    fun onUiEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.SwitcherChecked -> applyDarkTheme(event.checked)
            is SettingsUiEvent.ShareButtonClick -> shareLink()
            is SettingsUiEvent.SupportButtonClick -> writeToSupport()
            is SettingsUiEvent.UserAgreementButtonClick -> goToUserAgreement()
        }
    }

    private fun applyDarkTheme(enable: Boolean) {
        PlaylistMakerApp.switchTheme(enable)
        saveThemeUseCase.execute(enable)
        _darkThemeEnabled.value = enable
    }

    private fun shareLink() {
        shareLinkUseCase.execute()
    }

    private fun writeToSupport() {
        writeToSupportUseCase.execute()
    }

    private fun goToUserAgreement() {
        userAgreementUseCase.execute()
    }
}