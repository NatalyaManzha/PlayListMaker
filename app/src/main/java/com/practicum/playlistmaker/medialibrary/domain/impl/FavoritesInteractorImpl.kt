package com.practicum.playlistmaker.medialibrary.domain.impl

import com.practicum.playlistmaker.medialibrary.domain.api.FavoritesInteractor
import com.practicum.playlistmaker.medialibrary.domain.api.FavoritesRepository
import com.practicum.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(
    private val favoritesRepository: FavoritesRepository
) : FavoritesInteractor {
    override fun getFavoritesIdList(): Flow<List<Int>> {
        return favoritesRepository.getFavoritesIdList()
    }

    override fun getFavoritesFlow(): Flow<List<Track>> {
        return favoritesRepository.getFavoritesFlow()
    }

    override suspend fun insertFavorite(track: Track) {
        favoritesRepository.insertFavorite(track)
    }

    override suspend fun deleteFavorite(trackId: Int) {
        favoritesRepository.deleteFavorite(trackId)
    }

    override suspend fun checkTrackInFavorites(trackId: Int): Boolean {
        return favoritesRepository.checkTrackInFavorites(trackId)
    }
}