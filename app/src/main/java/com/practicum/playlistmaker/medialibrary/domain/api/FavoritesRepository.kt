package com.practicum.playlistmaker.medialibrary.domain.api

import com.practicum.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavoritesIdList(): Flow<List<Int>>
    suspend fun insertFavorite(track: Track)
    fun getFavoritesFlow(): Flow<List<Track>>
    suspend fun deleteFavorite(trackId: Int)
    suspend fun checkTrackInFavorites(trackId: Int): Boolean
}