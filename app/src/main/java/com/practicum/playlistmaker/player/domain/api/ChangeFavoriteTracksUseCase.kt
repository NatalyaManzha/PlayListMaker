package com.practicum.playlistmaker.player.domain.api

interface ChangeFavoriteTracksUseCase {
    fun addToFavorites(trackId: String)
    fun removeFromFavorites(trackId: String)
}