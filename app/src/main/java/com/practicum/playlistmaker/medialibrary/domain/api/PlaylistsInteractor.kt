package com.practicum.playlistmaker.medialibrary.domain.api

import com.practicum.playlistmaker.medialibrary.domain.models.Playlist
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    suspend fun insertPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlistID: Long)
    fun getPlaylistPreviewFlow(): Flow<List<PlaylistPreview>>
    suspend fun updatePlaylist(playlistID: Long, count: Int)
}