package com.practicum.playlistmaker.presentation.ui

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.domain.COMMAND_GET_CURRENT_POSITION
import com.practicum.playlistmaker.domain.COMMAND_GET_STATE
import com.practicum.playlistmaker.domain.COMMAND_PAUSE
import com.practicum.playlistmaker.domain.COMMAND_PREPARE
import com.practicum.playlistmaker.domain.COMMAND_RELEASE
import com.practicum.playlistmaker.domain.COMMAND_START
import com.practicum.playlistmaker.domain.STATE_PAUSED
import com.practicum.playlistmaker.domain.STATE_PLAYBACK_COMPLETE
import com.practicum.playlistmaker.domain.STATE_PLAYING
import com.practicum.playlistmaker.domain.STATE_PREPARED
import com.practicum.playlistmaker.domain.TRACK_TO_PLAY
import com.practicum.playlistmaker.domain.api.MediaPlayerInfoConsumer
import com.practicum.playlistmaker.domain.models.MediaPlayerControllerCommand
import com.practicum.playlistmaker.domain.models.MediaPlayerFeedbackData
import com.practicum.playlistmaker.domain.models.Track
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var track: Track
    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var updaterUi: MediaPlayerInfoConsumer
    private val handler = Handler(Looper.getMainLooper())
    private var runnable = Runnable { updatePlayerData() }
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private val mediaPlayerControler by lazy { Creator.provideControlMediaPlayerUseCase() }
    private var uiStateOnPlaying = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updaterUi = object : MediaPlayerInfoConsumer {
            override fun consume(info: MediaPlayerFeedbackData) {
                when (info) {
                    is MediaPlayerFeedbackData.State -> onPlayerStateChange(info.state)
                    is MediaPlayerFeedbackData.CurrentPosition -> onCurrentPositionChange(info.currentPosition)
                }
            }
        }

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

        prepareMediaPlayer()
    }

    private fun prepareMediaPlayer() {
        mediaPlayerControler.execute(
            MediaPlayerControllerCommand(
                COMMAND_PREPARE,
                track.previewUrl
            ), updaterUi
        )
    }

    private fun sendCommandToMediaPlayer(command: Int) {
        mediaPlayerControler.execute(MediaPlayerControllerCommand(command, null), updaterUi)
    }

    private fun onPlayerStateChange(state: Int) {
        with(binding) {
            when (state) {
                STATE_PREPARED -> {
                    playControlButton.isEnabled = true
                    uiStateOnPlaying = false
                }

                STATE_PLAYBACK_COMPLETE -> {
                    stopUpdatePlayerData()
                    playControlButton.setImageResource(R.drawable.button_play)
                    trackPlaytimeTV.text = dateFormat.format(0)
                    uiStateOnPlaying = false
                }

                STATE_PLAYING -> {
                    playControlButton.setImageResource(R.drawable.button_pause)
                    uiStateOnPlaying = true
                }

                STATE_PAUSED -> {
                    playControlButton.setImageResource(R.drawable.button_play)
                    uiStateOnPlaying = false
                }
            }
        }
    }

    private fun onCurrentPositionChange(currentPosition: Int) {
        binding.trackPlaytimeTV.text = dateFormat.format(currentPosition)
    }

    override fun onPause() {
        super.onPause()
        sendCommandToMediaPlayer(COMMAND_PAUSE)
        stopUpdatePlayerData()
    }

    override fun onDestroy() {
        super.onDestroy()
        sendCommandToMediaPlayer(COMMAND_RELEASE)
    }

    private fun updatePlayerData() {
        sendCommandToMediaPlayer(COMMAND_GET_CURRENT_POSITION)
        sendCommandToMediaPlayer(COMMAND_GET_STATE)
        handler.postDelayed(runnable, DELAY)
        Log.d("QQQ", "обновление времени")
    }

    private fun startUpdatePlayerData() {
        handler.post(runnable)
    }

    private fun stopUpdatePlayerData() {
     /*   handler.removeCallbacks(runnable)*/
        handler.removeCallbacksAndMessages(0)
        Log.d("QQQ", "обновление времени остановлено")
    }

    private fun playbackControl() {
        if (uiStateOnPlaying) {
            sendCommandToMediaPlayer(COMMAND_PAUSE)
            stopUpdatePlayerData()
        } else {
            sendCommandToMediaPlayer(COMMAND_START)
            startUpdatePlayerData()
        }
    }

    companion object {
        private const val DELAY = 300L
        private const val DELAY_MIN = 1000L
    }
}