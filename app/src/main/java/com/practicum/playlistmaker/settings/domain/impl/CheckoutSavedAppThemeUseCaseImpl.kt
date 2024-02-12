package com.practicum.playlistmaker.settings.domain.impl

import com.practicum.playlistmaker.settings.domain.api.AppThemeRepository
import com.practicum.playlistmaker.settings.domain.api.CheckoutSavedAppThemeUseCase

class CheckoutSavedAppThemeUseCaseImpl(
    private val appThemeRepository: AppThemeRepository
) : CheckoutSavedAppThemeUseCase {
    override fun execute(defaultStateOfDarkTheme: Boolean): Boolean {
        return appThemeRepository.checkoutSavedTheme(defaultStateOfDarkTheme)
    }

}