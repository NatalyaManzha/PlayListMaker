package com.practicum.playlistmaker.domain.api.useCase

interface SaveThemeUseCase {
    fun execute(darkThemeEnabled: Boolean)
}