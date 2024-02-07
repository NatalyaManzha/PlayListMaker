package com.practicum.playlistmaker.data.media_player

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.api.MediaPlayerControl
import com.practicum.playlistmaker.domain.models.MediaPlayerFeedbackData
import com.practicum.playlistmaker.domain.models.MediaPlayerState

class MediaPlayerControlImpl : MediaPlayerControl {

    private lateinit var url: String
    private val mediaPlayer = MediaPlayer()
    private var state = MediaPlayerState.DEFAULT


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
                state = MediaPlayerState.PREPARED
            }
            setOnCompletionListener {
                state = MediaPlayerState.PLAYBACK_COMPLETE
            }
        }
        return MediaPlayerFeedbackData.State(state)
    }

    override fun start(): MediaPlayerFeedbackData.State {
        mediaPlayer.start()
        state = MediaPlayerState.PLAYING
        return MediaPlayerFeedbackData.State(state)
    }

    override fun pause(): MediaPlayerFeedbackData.State {
        mediaPlayer.pause()
        state = MediaPlayerState.PAUSED
        return MediaPlayerFeedbackData.State(state)
    }

    override fun release(): MediaPlayerFeedbackData.State {
        mediaPlayer.release()
        state = MediaPlayerState.DEFAULT
        return MediaPlayerFeedbackData.State(state)
    }


}