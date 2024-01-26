package com.practicum.playlistmaker.domain.api.useCase

interface CheckoutSavedAppTheme {
    fun execute(defaultStateOfDarkTheme: Boolean): Boolean
}