package com.practicum.playlistmaker.medialibrary.data.impl


import android.util.Log
import com.practicum.playlistmaker.medialibrary.data.api.ImageStorage
import com.practicum.playlistmaker.medialibrary.data.converters.toPlaylistEntity
import com.practicum.playlistmaker.medialibrary.data.converters.toPreview
import com.practicum.playlistmaker.medialibrary.data.db.PlaylistsDatabase
import com.practicum.playlistmaker.medialibrary.domain.api.PlaylistsRepository
import com.practicum.playlistmaker.medialibrary.domain.models.NewPlaylist
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview
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
        //для теста
        val playlistTest = playlist.toPlaylistEntity(iconFileName)
        Log.d("QQQ", "playlistTest: $playlistTest")
        return playlistsDao.insertPlaylist(playlistTest)
    }

    override suspend fun deletePlaylist(playlistID: Long) {
        playlistsDao.deletePlaylist(playlistID)
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

    override suspend fun updatePlaylist(playlistID: Long, count: Int) {
        playlistsDao.updatePlaylist(playlistID, count)
    }

    companion object {
        private const val STRING_DEFAULT_VALUE = ""
    }
}