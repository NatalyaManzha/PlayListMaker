package com.practicum.playlistmaker.medialibrary.data.impl

import com.practicum.playlistmaker.medialibrary.data.converters.TrackEntityToTrackConverter
import com.practicum.playlistmaker.medialibrary.data.converters.TrackToTrackEntityConverter
import com.practicum.playlistmaker.medialibrary.data.db.FavoritesDatabase
import com.practicum.playlistmaker.medialibrary.domain.api.FavoritesRepository
import com.practicum.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    favoritesDB: FavoritesDatabase
) : FavoritesRepository {
    private val favoritesDao = favoritesDB.getFavoritesDao()
    override fun getFavoritesIdList(): Flow<List<Int>> {
        return favoritesDao.getFavoritesIdList()
    }

    override suspend fun insertFavorite(track: Track) {
        favoritesDao.insertFavorite(
            TrackToTrackEntityConverter.convert(track)
        )
    }

    override fun getFavoritesFlow(): Flow<List<Track>> {
        return favoritesDao.getFavoritesFlow()
            .map {
                TrackEntityToTrackConverter.convert(it)
            }
    }

    override suspend fun deleteFavorite(trackId: Int) {
        favoritesDao.deleteFavorite(trackId)
    }
}