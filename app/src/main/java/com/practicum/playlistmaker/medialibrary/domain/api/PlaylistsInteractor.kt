package com.practicum.playlistmaker.medialibrary.domain.api

import com.practicum.playlistmaker.medialibrary.domain.models.NewPlaylist
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview
import com.practicum.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    suspend fun insertPlaylist(playlist: NewPlaylist): Long
    fun getPlaylistPreviewFlow(): Flow<List<PlaylistPreview>>

    suspend fun addTrackToPlaylist(playlistID: Long, track: Track): Boolean

}