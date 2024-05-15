package com.practicum.playlistmaker.medialibrary.data.db.favorites

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [TrackEntity::class],
    exportSchema = false
)
abstract class FavoritesDatabase : RoomDatabase() {
    abstract fun getFavoritesDao(): FavoritesDao
}
