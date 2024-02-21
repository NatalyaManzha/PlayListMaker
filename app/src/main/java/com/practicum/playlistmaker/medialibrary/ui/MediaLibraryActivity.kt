package com.practicum.playlistmaker.medialibrary.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R

class MediaLibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_library)
    }

    companion object {
        fun show(context: Context) {
            val intent = Intent(context, MediaLibraryActivity::class.java)
            context.startActivity(intent)
        }
    }
}
