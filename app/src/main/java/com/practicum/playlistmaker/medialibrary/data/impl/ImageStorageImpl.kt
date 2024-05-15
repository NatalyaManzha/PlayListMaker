package com.practicum.playlistmaker.medialibrary.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import com.practicum.playlistmaker.medialibrary.data.api.ImageStorage
import java.io.File
import java.io.FileOutputStream
import java.util.Date

class ImageStorageImpl(
    private val appContext: Context
) : ImageStorage {
    private var filePath =
        File(appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), IMAGE_ALBUM)

    init {
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
    }

    override suspend fun saveImage(uri: Uri): String {
        val imageFileName = generateFileName()
        val file = File(filePath, imageFileName)
        try {
            val inputStream = appContext.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            BitmapFactory
                .decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        } catch (e: Exception) {
            Log.d("QQQ", "saveImage(): ${e.message}")
        }
        return imageFileName
    }

    override suspend fun uriByFileName(fileName: String): Uri? {
        return if (fileName.isEmpty()) null
        else File(filePath, fileName).toUri()
    }

    private fun generateFileName(): String {
        val dateFormat = SimpleDateFormat("ddMMYYYYmmss")
        val formattedDate = dateFormat.format(Date())
        return "$formattedDate.jpg"
    }

    companion object {
        private const val IMAGE_ALBUM = "image_album"
    }
}