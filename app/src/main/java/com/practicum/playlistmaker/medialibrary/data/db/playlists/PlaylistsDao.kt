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

    //получить количество треков, записанных в плейлист
    @Query("SELECT COUNT(*) FROM playlist_track_table WHERE playlistId = :playlistId")
    suspend fun getTracksCount(playlistId: Long): Int

    //получать список ID треков, записанных в плейлист, в виде потока
    @Query("SELECT trackId FROM playlist_track_table WHERE playlistId = :playlistId")
    fun getTrackIdListFlow(playlistId: Long): Flow<List<Int>>

    // обновить данные о количестве треков в плейлисте
    @Query("UPDATE playlist_table SET count = :count WHERE id = :playlistID")
    suspend fun updatePlaylistCount(playlistID: Long, count: Int)

    // обновить плейлист после редактирования
    @Query("UPDATE playlist_table SET iconFileName = :iconFileName, name = :name, description = :description WHERE id = :playlistID")
    suspend fun updatePlaylist(
        playlistID: Long,
        iconFileName: String,
        name: String,
        description: String
    )

    //получать в виде потока описание плейлиста
    @Query("SELECT * FROM playlist_table WHERE id = :playlistID")
    fun getPlaylistInfoFlow(playlistID: Long): Flow<PlaylistEntity>

    //получить трек по ID
    @Query("SELECT * FROM track_table WHERE trackId = :trackId")
    suspend fun getTrackByID(trackId: Int): TrackInPlaylistsEntity


    //ДОБАВЛЕНИЕ ТРЕКА В ПЛЕЙЛИСТ
    //1. проверить, есть ли такой трек в плейлисте
    @Query("SELECT COUNT(*) FROM playlist_track_table WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun checkTrackInPlaylist(playlistId: Long, trackId: Int): Int

    // 2. добавить трек в таблицу треков, занесенных в плейлисты, если такого еще нет
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackInPlaylistsEntity)

    // 3. добавить запись соотнесения трек-плейлист
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToPlaylist(trackToPlaylist: TrackToPlaylistEntity)


    //УДАЛЕНИЕ ТРЕКА ИЗ ПЛЕЙЛИСТА
    // 1. проверить, в скольких плейлистах присутствует трек по ID
    @Query("SELECT COUNT(*) FROM playlist_track_table WHERE trackId = :trackId")
    suspend fun checkPlaylistsCount(trackId: Int): Int

    // 2. удалить запись о принадлежности трека из плейлиста
    @Query("DELETE FROM playlist_track_table WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: Int)

    // 3. удалить трек из таблицы треков, занесенных в плейлисты
    @Query("DELETE FROM track_table WHERE trackId = :trackId")
    suspend fun deleteTrack(trackId: Int)

    // УДАЛЕНИЕ ПЛЕЙЛИСТА
    // 1. получить ID всех треков, относящихся к плейлисту
    @Query("SELECT trackId FROM playlist_track_table WHERE playlistId = :playlistId")
    suspend fun getTrackIdList(playlistId: Long): List<Int>

    // 2. удаление самого плейлиста
    @Query("DELETE FROM playlist_table WHERE id = :playlistID")
    suspend fun deletePlaylist(playlistID: Long)

    //проверка удаления
    @Query("SELECT COUNT(*) FROM playlist_table WHERE id = :playlistId")
    suspend fun checkPlaylistDeleted(playlistId: Long): Int
}


