package com.practicum.playlistmaker.medialibrary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [PlaylistEntity::class],
    exportSchema = false
)
abstract class PlaylistsDatabase : RoomDatabase() {
    abstract fun getPlaylistsDao(): PlaylistsDao
}
