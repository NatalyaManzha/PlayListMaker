package com.practicum.playlistmaker.medialibrary.data.db.playlists

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistsDao {
    //добавление плейлиста
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity): Long

    //получение списка плейлистов в виде превью
    @Query("SELECT id, iconFileName, name, count  FROM playlist_table")
    fun getPlaylistPreviewFlow(): Flow<List<PlaylistEntity>>


    //ДОБАВЛЕНИЕ ТРЕКА В ПЛЕЙЛИСТ
    //1. проверить, есть ли такой трек в плейлисте
    @Query("SELECT * FROM playlist_track_table WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun checkTrackInPlaylist(playlistId: Long, trackId: Int): List<TrackToPlaylistEntity>

    // 2. добавить трек в таблицу треков, занесенных в плейлисты, если такого еще нет
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackInPlaylistsEntity)

    // 3. добавить запись соотнесения трек-плейлист
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToPlaylist(trackToPlaylist: TrackToPlaylistEntity)


    //получить список ID треков, записанных в плейлист
    @Query("SELECT * FROM playlist_track_table WHERE playlistId = :playlistId")
    suspend fun getTrackIdList(playlistId: Long): List<TrackToPlaylistEntity>

    //получать список ID треков, записанных в плейлист, в виде потока
    @Query("SELECT trackId FROM playlist_track_table WHERE playlistId = :playlistId")
    fun getTrackIdListFlow(playlistId: Long): Flow<List<Int>>


    //получить трек по ID
    @Query("SELECT * FROM track_table WHERE trackId = :trackId")
    suspend fun getTrackByID(trackId: Int): TrackInPlaylistsEntity

    // обновить данные о количестве треков в плейлисте
    @Query("UPDATE playlist_table SET count = :count WHERE id = :playlistID")
    suspend fun updatePlaylistCount(playlistID: Long, count: Int)

    //получать в виде потока описание плейлиста
    @Query("SELECT * FROM playlist_table WHERE id = :playlistID")
    fun getPlaylistInfoFlow(playlistID: Long): Flow<PlaylistEntity>


    @Query("DELETE FROM playlist_table WHERE id = :playlistID")
    suspend fun deletePlaylist(playlistID: Long)
}


