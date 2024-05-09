package com.practicum.playlistmaker.medialibrary.data.impl


import com.practicum.playlistmaker.medialibrary.data.converters.converted
import com.practicum.playlistmaker.medialibrary.data.converters.toPlaylistEntity
import com.practicum.playlistmaker.medialibrary.data.converters.toPreview
import com.practicum.playlistmaker.medialibrary.data.db.PlaylistsDatabase
import com.practicum.playlistmaker.medialibrary.domain.api.PlaylistsRepository
import com.practicum.playlistmaker.medialibrary.domain.models.Playlist
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    playlistsDB: PlaylistsDatabase
): PlaylistsRepository {
    private val playlistsDao = playlistsDB.getPlaylistsDao()
    override suspend fun insertPlaylist(playlist: Playlist) {
        playlistsDao.insertPlaylist(
            playlist.toPlaylistEntity()
        )
    }

    override suspend fun deletePlaylist(playlistID: Long) {
        playlistsDao.deletePlaylist(playlistID)
    }

    override fun getPlaylistPreviewFlow(): Flow<List<PlaylistPreview>> {
       return playlistsDao.getPlaylistPreviewFlow().map {
           it.converted()
       }
    }

    override suspend fun updatePlaylist(playlistID: Long, count: Int) {
        playlistsDao.updatePlaylist(playlistID, count)
    }
}