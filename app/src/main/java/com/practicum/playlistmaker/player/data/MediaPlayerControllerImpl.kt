package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.api.MediaPlayerController
import com.practicum.playlistmaker.player.domain.models.MediaPlayerFeedbackData
import com.practicum.playlistmaker.player.domain.models.MediaPlayerState

class MediaPlayerControllerImpl(
    private val mediaPlayer: MediaPlayer
) : MediaPlayerController {

    private lateinit var url: String
    private var state = MediaPlayerState.DEFAULT


    override fun getMediaPlayerState(): MediaPlayerFeedbackData.State {
        return MediaPlayerFeedbackData.State(state)
    }

    override fun getCurrentPosition(): MediaPlayerFeedbackData.CurrentPosition {
        val currentPosition = TimeFormatter.format(mediaPlayer.currentPosition)
        return MediaPlayerFeedbackData.CurrentPosition(currentPosition)
    }

    override fun prepare(url: String): MediaPlayerFeedbackData.State {
        with(mediaPlayer) {
            try {
                setDataSource(url)
                prepareAsync()
            } catch (error: Throwable) {
                return MediaPlayerFeedbackData.State(MediaPlayerState.ERROR)
            }
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