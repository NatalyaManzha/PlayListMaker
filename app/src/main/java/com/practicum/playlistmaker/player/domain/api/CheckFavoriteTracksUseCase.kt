package com.practicum.playlistmaker.player.domain.api

interface CheckFavoriteTracksUseCase {
    fun isInFavorites (trackId:String): Boolean
}