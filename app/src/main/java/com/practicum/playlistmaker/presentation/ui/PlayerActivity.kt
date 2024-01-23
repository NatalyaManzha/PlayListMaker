package com.practicum.playlistmaker.presentation.ui

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.TRACK_TO_PLAY
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.domain.models.Track
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var track: Track
    private lateinit var binding: ActivityAudioPlayerBinding
    private var playerState = STATE_DEFAULT
    private val mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())
    private var runnable = Runnable { updatePlayTime() }
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = intent.getSerializableExtra(TRACK_TO_PLAY) as Track

        with(binding) {
            backButton.setOnClickListener {
                finish()
            }
            trackNameTV.text = track.trackName
            artistNameTV.text = track.artistName
            trackPlaytimeTV.text = dateFormat.format(0)
            durationTV.text = track.getFormatedTime()
            albumTV.text = track.collectionName
            yearTV.text = track.getReleaseYear()
            genreTV.text = track.primaryGenreName
            countryTV.text = track.country
            playControlButton.setOnClickListener {
                playbackControl()
            }
        }

        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(applicationContext.resources.getDimensionPixelSize(R.dimen.radius_8dp)))
            .into(binding.coverArtwork)

        preparePlayer()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
        stopUpdatePlayTime()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    fun updatePlayTime() {
        binding.trackPlaytimeTV.text = dateFormat.format(mediaPlayer.currentPosition)
        handler.postDelayed(runnable, DELAY)
    }

    private fun startUpdatePlayTime() {
        handler.post(runnable)
    }

    private fun stopUpdatePlayTime() {
        handler.removeCallbacks(runnable)
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
                stopUpdatePlayTime()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
                startUpdatePlayTime()
            }
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.playControlButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            binding.playControlButton.setImageResource(R.drawable.button_play)
            playerState = STATE_PREPARED
            stopUpdatePlayTime()
            binding.trackPlaytimeTV.text = dateFormat.format(0)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.playControlButton.setImageResource(R.drawable.button_pause)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.playControlButton.setImageResource(R.drawable.button_play)
        playerState = STATE_PAUSED
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 300L
    }
}