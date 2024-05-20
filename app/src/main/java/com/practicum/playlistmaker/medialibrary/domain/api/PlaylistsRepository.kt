package com.practicum.playlistmaker.medialibrary.domain.api

import com.practicum.playlistmaker.medialibrary.domain.models.EditPlaylist
import com.practicum.playlistmaker.medialibrary.domain.models.NewPlaylist
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistInfo
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview
import com.practicum.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    suspend fun insertPlaylist(playlist: NewPlaylist): Long
    fun getPlaylistPreviewFlow(): Flow<List<PlaylistPreview>>
    suspend fun addTrackToPlaylist(playlistId: Long, track: Track): Boolean
    fun getPlaylistInfoFlow(playlistID: Long): Flow<PlaylistInfo>
    fun getTrackIdListFlow(playlistId: Long): Flow<List<Int>>
    suspend fun getTrackByID(trackId: Int): Track
    suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: Int)
    suspend fun deletePlaylist(playlistId: Long): Boolean
    suspend fun updatePlaylist(playlistInfo: EditPlaylist)

}