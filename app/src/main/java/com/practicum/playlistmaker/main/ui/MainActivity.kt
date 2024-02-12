package com.practicum.playlistmaker.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.medialibrary.ui.MediaLibraryActivity
import com.practicum.playlistmaker.search.ui.SearchActivity
import com.practicum.playlistmaker.settings.ui.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {

            searchButton.setOnClickListener {
                SearchActivity.show(this@MainActivity)
            }

            mediaLibraryButton.setOnClickListener {
                MediaLibraryActivity.show(this@MainActivity)
            }

            settingsButton.setOnClickListener {
                SettingsActivity.show(this@MainActivity)
            }
        }
    }
}