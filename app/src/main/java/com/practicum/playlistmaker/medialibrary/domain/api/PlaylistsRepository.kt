package com.practicum.playlistmaker.medialibrary.domain.api

import com.practicum.playlistmaker.medialibrary.data.db.playlists.PlaylistEntity
import com.practicum.playlistmaker.medialibrary.domain.models.NewPlaylist
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview
import com.practicum.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    suspend fun insertPlaylist(playlist: NewPlaylist): Long
    fun getPlaylistPreviewFlow(): Flow<List<PlaylistPreview>>
    suspend fun addTrackToPlaylist(playlistId: Long, track: Track): Boolean







    suspend fun deletePlaylist(playlistID: Long)

    suspend fun updatePlaylist(playlistID: Long, count: Int)
}