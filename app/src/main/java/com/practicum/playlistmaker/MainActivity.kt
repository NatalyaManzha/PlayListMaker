package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.search)
        buttonSearch.setOnClickListener {
            val intent = Intent (this, SearchActivity::class.java)
            startActivity(intent)
        }

        val buttonMediaLibrary = findViewById<Button>(R.id.media_library)
        buttonMediaLibrary.setOnClickListener {
            val intent = Intent (this, MediaLibraryActivity::class.java)
            startActivity(intent)
        }
        val buttonSetting = findViewById<Button>(R.id.settings)
        buttonSetting.setOnClickListener {
            val intent = Intent (this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}