package com.practicum.playlistmaker.domain.api.useCase

interface CheckoutSavedAppThemeUseCase {
    fun execute(defaultStateOfDarkTheme: Boolean): Boolean
}