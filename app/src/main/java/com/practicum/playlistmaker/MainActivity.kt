package com.practicum.playlistmaker

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class PlaylistMakerApp : Application() {
    lateinit var trackData: TrackData
}
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search)
        searchButton.setOnClickListener {
            val intent = Intent (this, SearchActivity::class.java)
            startActivity(intent)
        }

        val mediaLibraryButton = findViewById<Button>(R.id.media_library)
        mediaLibraryButton.setOnClickListener {
            val intent = Intent (this, MediaLibraryActivity::class.java)
            startActivity(intent)
        }
        val settingsButton = findViewById<Button>(R.id.settings)
        settingsButton.setOnClickListener {
            val intent = Intent (this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}