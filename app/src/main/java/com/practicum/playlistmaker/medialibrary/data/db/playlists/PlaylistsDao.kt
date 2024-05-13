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

//добавление трека в
// 1. добавить трек в таблицу треков, занесенных в плейлисты
@Insert(onConflict = OnConflictStrategy.IGNORE)
suspend fun insertTrack(track:TrackInPlaylistsEntity)
// 2. добавить запись соотнесения трек-плейлист
@Insert(onConflict = OnConflictStrategy.IGNORE)
suspend fun addTrackToPlaylist(trackToPlaylist: TrackToPlaylistEntity)
//проверить, есть ли такой трек в плейлисте
@Query("SELECT * FROM playlist_track_table WHERE playlistId = :playlistId AND trackId = :trackId")
suspend fun checkTrackInPlaylist(playlistId: Long, trackId: Int): List<TrackToPlaylistEntity>
//получить список ID треков, записанных в плейлист
@Query("SELECT * FROM playlist_track_table WHERE playlistId = :playlistId")
suspend fun getTrackIdList(playlistId: Long): List<TrackToPlaylistEntity>






//получить сам плейлист по ID
@Query("SELECT * FROM playlist_table WHERE id = :playlistID")
suspend fun getPlaylistByID(playlistID: Long): PlaylistEntity



    @Query("DELETE FROM playlist_table WHERE id = :playlistID")
    suspend fun deletePlaylist(playlistID: Long)



    @Query("UPDATE playlist_table SET count = :count WHERE id = :playlistID")
    suspend fun updatePlaylist(playlistID: Long, count: Int)
}