package com.practicum.playlistmaker.domain.impl.useCase.app_theme

import com.practicum.playlistmaker.domain.api.AppThemeRepository
import com.practicum.playlistmaker.domain.api.useCase.SaveThemeUseCase

class SaveThemeUseCaseImpl(
    private val appThemeRepository: AppThemeRepository
) : SaveThemeUseCase {
    override fun execute(darkThemeEnabled: Boolean) {
        appThemeRepository.saveTheme(darkThemeEnabled)
    }
}