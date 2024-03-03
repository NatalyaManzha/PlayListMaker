package com.practicum.playlistmaker.medialibrary.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaLibraryBinding

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        with(binding) {
            setContentView(root)
            mediaLibraryToolbar.setNavigationOnClickListener {
                this@MediaLibraryActivity.onBackPressedDispatcher.onBackPressed()
            }
            viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
            tabMediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = resources.getString(R.string.favorites)
                    1 -> tab.text = resources.getString(R.string.playlists)
                }
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

    companion object {
        fun show(context: Context) {
            val intent = Intent(context, MediaLibraryActivity::class.java)
            context.startActivity(intent)
        }
    }
}
