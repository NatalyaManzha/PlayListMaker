package com.practicum.playlistmaker.medialibrary.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Query("DELETE FROM playlist_table WHERE id = :playlistID")
    suspend fun deletePlaylist(playlistID: Long)

    @Query("SELECT id, iconUrl, name, count  FROM playlist_table")
    fun getPlaylistPreviewFlow(): Flow<List<PlaylistEntity>>

    @Query("UPDATE playlist_table SET count = :count WHERE id = :playlistID")
    suspend fun updatePlaylist(playlistID: Long, count: Int)
}