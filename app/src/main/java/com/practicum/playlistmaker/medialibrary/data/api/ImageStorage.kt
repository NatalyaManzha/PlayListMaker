package com.practicum.playlistmaker.medialibrary.data.api

import android.net.Uri

interface ImageStorage {
    suspend fun saveImage(uri: Uri): String
    suspend fun uriByFileName(fileName: String): Uri?
}