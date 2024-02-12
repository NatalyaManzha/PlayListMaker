package com.practicum.playlistmaker.settings.domain.api

interface SaveThemeUseCase {
    fun execute(darkThemeEnabled: Boolean)
}