package com.practicum.playlistmaker.medialibrary.data.impl


import com.practicum.playlistmaker.medialibrary.data.api.ImageStorage
import com.practicum.playlistmaker.medialibrary.data.converters.toPlaylistEntity
import com.practicum.playlistmaker.medialibrary.data.converters.toPreview
import com.practicum.playlistmaker.medialibrary.data.converters.toTrackInPlaylistsEntity
import com.practicum.playlistmaker.medialibrary.data.db.playlists.PlaylistsDatabase
import com.practicum.playlistmaker.medialibrary.data.db.playlists.TrackInPlaylistsEntity
import com.practicum.playlistmaker.medialibrary.data.db.playlists.TrackToPlaylistEntity
import com.practicum.playlistmaker.medialibrary.domain.api.PlaylistsRepository
import com.practicum.playlistmaker.medialibrary.domain.models.NewPlaylist
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview
import com.practicum.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    private val playlistsDB: PlaylistsDatabase,
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

    override suspend fun addTrackToPlaylist(playlistId: Long, track: Track): Boolean{

            val tracks = playlistsDao.checkTrackInPlaylist(playlistId, track.trackId)
        if (tracks.isNotEmpty()) return false
        else {
            playlistsDao.addTrackToPlaylist(TrackToPlaylistEntity(null, playlistId, track.trackId))
            playlistsDao.insertTrack(track.toTrackInPlaylistsEntity())
           val count = playlistsDao.getTrackIdList(playlistId).size
            playlistsDao.updatePlaylist(playlistId, count)
            return true
        }
    }



    override suspend fun deletePlaylist(playlistID: Long) {
        playlistsDao.deletePlaylist(playlistID)
    }
    override suspend fun updatePlaylist(playlistID: Long, count: Int) {
        playlistsDao.updatePlaylist(playlistID, count)
    }

    companion object {
        private const val STRING_DEFAULT_VALUE = ""
    }
}