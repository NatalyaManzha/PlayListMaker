package com.practicum.playlistmaker.medialibrary.domain.api

import com.practicum.playlistmaker.medialibrary.data.db.PlaylistEntity
import com.practicum.playlistmaker.medialibrary.domain.models.Playlist
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview
import com.practicum.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    suspend fun insertPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlistID: Long)
    fun getPlaylistPreviewFlow(): Flow<List<PlaylistPreview>>
    suspend fun updatePlaylist(playlistID: Long, count: Int)
}