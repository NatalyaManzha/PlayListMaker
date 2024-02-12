package com.practicum.playlistmaker.settings.domain.impl

import com.practicum.playlistmaker.settings.domain.api.AppThemeRepository
import com.practicum.playlistmaker.settings.domain.api.SaveThemeUseCase

class SaveThemeUseCaseImpl(
    private val appThemeRepository: AppThemeRepository
) : SaveThemeUseCase {
    override fun execute(darkThemeEnabled: Boolean) {
        appThemeRepository.saveTheme(darkThemeEnabled)
    }
}