package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.domain.api.CheckFavoriteTracksUseCase
import com.practicum.playlistmaker.player.domain.api.FavoriteTracksRepository

class CheckFavoriteTracksUseCaseImpl(
    private val favorites: FavoriteTracksRepository
) : CheckFavoriteTracksUseCase {
    override fun isInFavorites(trackId: String): Boolean {
        return favorites.checkFavorites(trackId)
    }
}