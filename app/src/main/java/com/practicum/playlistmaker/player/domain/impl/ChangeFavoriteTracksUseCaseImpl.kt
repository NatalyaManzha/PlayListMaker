package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.domain.api.ChangeFavoriteTracksUseCase
import com.practicum.playlistmaker.player.domain.api.FavoriteTracksRepository

class ChangeFavoriteTracksUseCaseImpl(
    private val favorites: FavoriteTracksRepository
) : ChangeFavoriteTracksUseCase {
    override fun addToFavorites(trackId: String) {
        favorites.addToFavorites(trackId)
    }

    override fun removeFromFavorites(trackId: String) {
        favorites.removeFromFavorites(trackId)
    }
}