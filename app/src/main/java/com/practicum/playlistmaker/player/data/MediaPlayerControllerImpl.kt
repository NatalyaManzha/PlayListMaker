package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.api.MediaPlayerController
import com.practicum.playlistmaker.player.domain.models.MediaPlayerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MediaPlayerControllerImpl(
    private val mediaPlayer: MediaPlayer
) : MediaPlayerController {

    private lateinit var url: String
    private var state = MediaPlayerState.DEFAULT

    /** Холодный поток, каждые 0,3 сек проверяющий состояние плеера
     * Отправляет только новое состояние
     */
    override fun updateState(): Flow<MediaPlayerState> = flow {
        var emitedState: MediaPlayerState? = null
        while (true) {
            var stateToEmit = state
            if (stateToEmit != emitedState) {
                emit(stateToEmit)
                emitedState = stateToEmit
            }
            delay(DELAY_MILLIS)
        }
    }

    /** Холодный поток, каждые 0,3 сек обновляющий время воспроизведения
     */
    override fun updateProgress(): Flow<String> = flow {
        while (true) {
            emit(TimeFormatter.format(mediaPlayer.currentPosition))
            delay(DELAY_MILLIS)
        }
    }


    override fun prepare(url: String) {
        with(mediaPlayer) {
            try {
                setDataSource(url)
                prepareAsync()
            } catch (error: Throwable) {
                state = MediaPlayerState.ERROR
            }
            setOnPreparedListener {
                state = MediaPlayerState.PREPARED
            }
            setOnCompletionListener {
                state = MediaPlayerState.PLAYBACK_COMPLETE
            }
        }
    }

    override fun start() {
        mediaPlayer.start()
        state = MediaPlayerState.PLAYING
    }

    override fun pause() {
        mediaPlayer.pause()
        state = MediaPlayerState.PAUSED
    }

    override fun release() {
        mediaPlayer.release()
        state = MediaPlayerState.DEFAULT
    }

    companion object {
        private const val DELAY_MILLIS = 300L
    }
}