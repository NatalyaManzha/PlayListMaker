package com.practicum.playlistmaker.settings.ui.models

sealed interface SettingsUiEvent {
    data class SwitcherChecked(val checked: Boolean) : SettingsUiEvent
    object ShareButtonClick : SettingsUiEvent
    object SupportButtonClick : SettingsUiEvent
    object UserAgreementButtonClick : SettingsUiEvent
}