package com.practicum.playlistmaker.data.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.STATE_DEFAULT
import com.practicum.playlistmaker.domain.STATE_PAUSED
import com.practicum.playlistmaker.domain.STATE_PLAYBACK_COMPLETE
import com.practicum.playlistmaker.domain.STATE_PLAYING
import com.practicum.playlistmaker.domain.STATE_PREPARED
import com.practicum.playlistmaker.domain.api.MediaPlayerControl
import com.practicum.playlistmaker.domain.models.MediaPlayerFeedbackData

class MediaPlayerControlImpl : MediaPlayerControl {

    private lateinit var url: String
    private val mediaPlayer = MediaPlayer()
    private var state = STATE_DEFAULT


    override fun getMediaPlayerState(): MediaPlayerFeedbackData.State {
        return MediaPlayerFeedbackData.State(state)
    }

    override fun getCurrentPosition(): MediaPlayerFeedbackData.CurrentPosition {
        val currentPosition = mediaPlayer.currentPosition
        return MediaPlayerFeedbackData.CurrentPosition(currentPosition)
    }

    override fun prepare(url: String): MediaPlayerFeedbackData.State {
        with(mediaPlayer) {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener {
                state = STATE_PREPARED
            }
            setOnCompletionListener {
                state = STATE_PLAYBACK_COMPLETE
            }
        }
        return MediaPlayerFeedbackData.State(state)
    }

    override fun start(): MediaPlayerFeedbackData.State {
        mediaPlayer.start()
        state = STATE_PLAYING
        return MediaPlayerFeedbackData.State(state)
    }

    override fun pause(): MediaPlayerFeedbackData.State {
        mediaPlayer.pause()
        state = STATE_PAUSED
        return MediaPlayerFeedbackData.State(state)
    }

    override fun release(): MediaPlayerFeedbackData.State {
        mediaPlayer.release()
        state = STATE_DEFAULT
        return MediaPlayerFeedbackData.State(state)
    }


}