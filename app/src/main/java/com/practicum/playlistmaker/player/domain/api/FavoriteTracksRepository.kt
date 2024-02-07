package com.practicum.playlistmaker.player.domain.api

interface FavoriteTracksRepository {
    fun addToFavorites(trackId: String)
    fun removeFromFavorites(trackId: String)
    fun checkFavorites(trackId: String) : Boolean
}