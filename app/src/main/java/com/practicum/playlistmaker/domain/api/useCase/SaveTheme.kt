package com.practicum.playlistmaker.domain.api.useCase

interface SaveTheme {
    fun execute(darkThemeEnabled: Boolean)
}