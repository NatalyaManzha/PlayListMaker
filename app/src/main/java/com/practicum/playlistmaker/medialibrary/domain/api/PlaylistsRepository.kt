package com.practicum.playlistmaker.medialibrary.domain.api

import com.practicum.playlistmaker.medialibrary.domain.models.NewPlaylist
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    suspend fun insertPlaylist(playlist: NewPlaylist)
    suspend fun deletePlaylist(playlistID: Long)
    fun getPlaylistPreviewFlow(): Flow<List<PlaylistPreview>>
    suspend fun updatePlaylist(playlistID: Long, count: Int)
}