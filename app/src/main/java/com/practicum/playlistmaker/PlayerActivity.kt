package com.practicum.playlistmaker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var track: Track
    private lateinit var binding: ActivityAudioPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = intent.getSerializableExtra(TRACK_TO_PLAY) as Track
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.trackNameTV.text = track.trackName
        binding.artistNameTV.text = track.artistName
        binding.trackPlaytimeTV.text = "0:56" // до реализации логики
        binding.durationTV.text = track.getFormatedTime()
        binding.albumTV.text = track.collectionName
        binding.yearTV.text = track.releaseDate.substring(0, 4)
        binding.genreTV.text = track.primaryGenreName
        binding.countryTV.text = track.country

        Glide.with(this)
            .load(getCoverArtwork(track.artworkUrl100))
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(applicationContext.resources.getDimensionPixelSize(R.dimen.radius_8dp)))
            .into(binding.coverArtwork)
    }

    fun getCoverArtwork(imageUrl: String) = imageUrl.replaceAfterLast('/', "512x512bb.jpg")
}