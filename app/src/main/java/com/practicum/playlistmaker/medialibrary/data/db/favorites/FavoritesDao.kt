package com.practicum.playlistmaker.medialibrary.data.db.favorites

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(trackEntity: TrackEntity)

    @Query("DELETE FROM favorites_table WHERE trackId = :trackId")
    suspend fun deleteFavorite(trackId: Int)

    @Query("SELECT * FROM favorites_table")
    fun getFavoritesFlow(): Flow<List<TrackEntity>>

    @Query("SELECT trackId FROM favorites_table")
    fun getFavoritesIdList(): Flow<List<Int>>

    @Query("SELECT COUNT(*) FROM favorites_table WHERE trackId = :trackId")
    suspend fun checkTrackInFavorites(trackId: Int): Int
}
