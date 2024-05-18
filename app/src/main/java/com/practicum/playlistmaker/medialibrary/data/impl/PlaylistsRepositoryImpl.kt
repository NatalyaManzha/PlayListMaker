package com.practicum.playlistmaker.medialibrary.data.impl


import com.practicum.playlistmaker.medialibrary.data.api.ImageStorage
import com.practicum.playlistmaker.medialibrary.data.converters.toPlaylistEntity
import com.practicum.playlistmaker.medialibrary.data.converters.toPlaylistInfo
import com.practicum.playlistmaker.medialibrary.data.converters.toPreview
import com.practicum.playlistmaker.medialibrary.data.converters.toTrack
import com.practicum.playlistmaker.medialibrary.data.converters.toTrackInPlaylistsEntity
import com.practicum.playlistmaker.medialibrary.data.db.playlists.PlaylistsDatabase
import com.practicum.playlistmaker.medialibrary.data.db.playlists.TrackToPlaylistEntity
import com.practicum.playlistmaker.medialibrary.domain.api.PlaylistsRepository
import com.practicum.playlistmaker.medialibrary.domain.models.NewPlaylist
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistInfo
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview
import com.practicum.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    playlistsDB: PlaylistsDatabase,
    private val imageStorage: ImageStorage
) : PlaylistsRepository {

    private val playlistsDao = playlistsDB.getPlaylistsDao()

    override suspend fun insertPlaylist(playlist: NewPlaylist): Long {
        var iconFileName = STRING_DEFAULT_VALUE
        with(playlist.iconUri) {
            if (this != null) iconFileName = imageStorage.saveImage(this)
        }
        return playlistsDao.insertPlaylist(
            playlist.toPlaylistEntity(iconFileName)
        )
    }

    override fun getPlaylistPreviewFlow(): Flow<List<PlaylistPreview>> {
        return playlistsDao.getPlaylistPreviewFlow().map { list ->
            list.map { playlistEntity ->
                playlistEntity.toPreview(
                    imageStorage.uriByFileName(playlistEntity.iconFileName)
                )
            }
        }
    }

    override suspend fun addTrackToPlaylist(playlistId: Long, track: Track): Boolean {
        val tracks = playlistsDao.checkTrackInPlaylist(playlistId, track.trackId)
        return if (tracks.isNotEmpty()) false
        else {
            with(playlistsDao) {
                addTrackToPlaylist(TrackToPlaylistEntity(null, playlistId, track.trackId))
                insertTrack(track.toTrackInPlaylistsEntity())
                val count = getTrackIdList(playlistId).size
                updatePlaylistCount(playlistId, count)
            }
            true
        }
    }

    override fun getPlaylistInfoFlow(playlistID: Long): Flow<PlaylistInfo> {
        return playlistsDao.getPlaylistInfoFlow(playlistID).map { playlistEntity ->
            playlistEntity.toPlaylistInfo(
                imageStorage.uriByFileName(playlistEntity.iconFileName)
            )
        }
    }

    override fun getTrackIdListFlow(playlistId: Long): Flow<List<Int>> {
        return playlistsDao.getTrackIdListFlow(playlistId)
    }

    override suspend fun getTrackByID(trackId: Int): Track {
        return playlistsDao.getTrackByID(trackId).toTrack()
    }

    companion object {
        private const val STRING_DEFAULT_VALUE = ""
    }
}