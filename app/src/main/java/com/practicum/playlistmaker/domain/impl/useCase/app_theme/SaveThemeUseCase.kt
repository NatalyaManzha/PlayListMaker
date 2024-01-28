package com.practicum.playlistmaker.domain.impl.useCase.app_theme

import com.practicum.playlistmaker.domain.api.AppThemeRepository
import com.practicum.playlistmaker.domain.api.useCase.SaveTheme

class SaveThemeUseCase(
    private val appThemeRepository: AppThemeRepository
) : SaveTheme {
    override fun execute(darkThemeEnabled: Boolean) {
        appThemeRepository.saveTheme(darkThemeEnabled)
    }
}