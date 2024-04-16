package com.practicum.playlistmaker.medialibrary.domain.api

import com.practicum.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    fun getFavoritesIdList(): Flow<List<Int>>
    fun getFavoritesFlow(): Flow<List<Track>>
    suspend fun insertFavorite(track: Track)
    suspend fun deleteFavorite(trackId: Int)
}