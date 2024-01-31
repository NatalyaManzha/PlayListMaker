package com.practicum.playlistmaker.presentation.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.domain.TRACK_TO_PLAY
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.api.PlayerUiUpdater
import com.practicum.playlistmaker.presentation.presenter.PlayerUiInteractor

class PlayerActivity : AppCompatActivity() {

    private lateinit var track: Track
    private lateinit var binding: ActivityAudioPlayerBinding
    private var uiStateOnPlaying = false
    private lateinit var playerUiInteractor: PlayerUiInteractor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val playerUiUpdater = with(binding) {
            object : PlayerUiUpdater {
                override fun onPlayerStatePrepared() {
                    playControlButton.isEnabled = true
                    uiStateOnPlaying = false
                }

                override fun onPlayerStatePlaybackComplete() {
                    playControlButton.setImageResource(R.drawable.button_play)
                    trackPlaytimeTV.text = PLAYER_START_TIME
                    uiStateOnPlaying = false
                }

                override fun onPlayerStatePlaying() {
                    playControlButton.setImageResource(R.drawable.button_pause)
                    uiStateOnPlaying = true
                }

                override fun onPlayerStatePaused() {
                    playControlButton.setImageResource(R.drawable.button_play)
                    uiStateOnPlaying = false
                }

                override fun onCurrentPositionChange(currentPosition: String) {
                    binding.trackPlaytimeTV.text = currentPosition
                }

                override fun getPlayerActivityUiState(): Boolean {
                    return uiStateOnPlaying
                }
            }
        }
        playerUiInteractor = PlayerUiInteractor(playerUiUpdater)

        track = intent.getSerializableExtra(TRACK_TO_PLAY) as Track

        with(binding) {
            backButton.setOnClickListener {
                finish()
            }
            trackNameTV.text = track.trackName
            artistNameTV.text = track.artistName
            trackPlaytimeTV.text = PLAYER_START_TIME
            durationTV.text = track.trackTimeMinSec
            albumTV.text = track.collectionName
            yearTV.text = track.releaseYear
            genreTV.text = track.primaryGenreName
            countryTV.text = track.country
            playControlButton.setOnClickListener {
                playerUiInteractor.playbackControl(uiStateOnPlaying)
            }
        }
        val startTime = System.currentTimeMillis()
        Glide.with(this)
            .load(track.artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(applicationContext.resources.getDimensionPixelSize(R.dimen.radius_8dp)))
            .into(binding.coverArtwork)
        val endTime = System.currentTimeMillis()
        Log.d("QQQ", "Картинка загружалась ${endTime-startTime} миллисек")

        playerUiInteractor.prepareUiInteractor(track)
    }

    override fun onPause() {
        super.onPause()
        playerUiInteractor.onPlayerActivityPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerUiInteractor.onPlayerActivityDestroy()
    }

    companion object {
        private const val PLAYER_START_TIME = "00:00"
    }
}