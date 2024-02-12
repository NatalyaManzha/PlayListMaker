package com.practicum.playlistmaker.settings.domain.api

interface CheckoutSavedAppThemeUseCase {
    fun execute(defaultStateOfDarkTheme: Boolean): Boolean
}