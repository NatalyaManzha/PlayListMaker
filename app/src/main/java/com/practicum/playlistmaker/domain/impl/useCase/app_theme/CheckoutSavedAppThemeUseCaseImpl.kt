package com.practicum.playlistmaker.domain.impl.useCase.app_theme

import com.practicum.playlistmaker.domain.api.AppThemeRepository
import com.practicum.playlistmaker.domain.api.useCase.CheckoutSavedAppThemeUseCase

class CheckoutSavedAppThemeUseCaseImpl(
    private val appThemeRepository: AppThemeRepository
) : CheckoutSavedAppThemeUseCase {
    override fun execute(defaultStateOfDarkTheme: Boolean): Boolean {
        return appThemeRepository.checkoutSavedTheme(defaultStateOfDarkTheme)
    }

}