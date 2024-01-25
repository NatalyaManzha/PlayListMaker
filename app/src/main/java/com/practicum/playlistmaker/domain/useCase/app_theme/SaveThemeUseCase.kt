package com.practicum.playlistmaker.domain.useCase.app_theme

import com.practicum.playlistmaker.domain.api.AppThemeRepository

class SaveThemeUseCase(
    private val appThemeRepository: AppThemeRepository
) {
    fun execute(darkThemeEnabled: Boolean) {
        appThemeRepository.saveTheme(darkThemeEnabled)
    }
}