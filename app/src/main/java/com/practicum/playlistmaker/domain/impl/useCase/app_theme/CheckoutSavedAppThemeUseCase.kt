package com.practicum.playlistmaker.domain.impl.useCase.app_theme

import com.practicum.playlistmaker.domain.api.AppThemeRepository
import com.practicum.playlistmaker.domain.api.useCase.CheckoutSavedAppTheme

class CheckoutSavedAppThemeUseCase(
    private val appThemeRepository: AppThemeRepository
) : CheckoutSavedAppTheme {
    override fun execute(defaultStateOfDarkTheme: Boolean): Boolean {
        return appThemeRepository.checkoutSavedTheme(defaultStateOfDarkTheme)
    }

}