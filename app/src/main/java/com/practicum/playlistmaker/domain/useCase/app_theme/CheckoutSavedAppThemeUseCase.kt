package com.practicum.playlistmaker.domain.useCase.app_theme

import com.practicum.playlistmaker.domain.api.AppThemeRepository

class CheckoutSavedAppThemeUseCase(
    private val appThemeRepository: AppThemeRepository
) {
    fun execute(defaultStateOfDarkTheme: Boolean): Boolean {
        return appThemeRepository.checkoutSavedTheme(defaultStateOfDarkTheme)
    }

}