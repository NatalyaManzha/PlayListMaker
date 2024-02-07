package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.domain.TRACK_TO_PLAY
import com.practicum.playlistmaker.player.domain.models.Track

class PlayerActivity : AppCompatActivity() {

    private lateinit var track: Track
    private lateinit var binding: ActivityPlayerBinding
    private var uiStateOnPlaying = false
    private lateinit var viewModel: PlayerViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
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

                override fun setResourseToFavoritesButton(isInFavorites: Boolean) {
                    val resourse =
                        if (isInFavorites) R.drawable.remove_from_favorites else R.drawable.add_to_favorites
                    binding.addToFavoritesButton.setImageResource(resourse)
                }
            }
        }
        track = intent.getSerializableExtra(TRACK_TO_PLAY) as Track
        viewModel = PlayerViewModel(playerUiUpdater)
        viewModel.checkFavorites(track.trackId)


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
                viewModel.playbackControl(uiStateOnPlaying)
            }
            addToFavoritesButton.setOnClickListener {
                viewModel.toggleFavorite(track.trackId)
            }
        }

        Glide.with(this)
            .load(track.artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(applicationContext.resources.getDimensionPixelSize(R.dimen.radius_8dp)))
            .into(binding.coverArtwork)

        viewModel.prepareMediaPlayer(track.previewUrl)
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPlayerActivityPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onPlayerActivityDestroy()
    }

    /* private fun chooseResourseToFavoritesButton(isInFavorites: Boolean): Int {
         return if (isInFavorites) R.drawable.add_to_favorites else R.drawable.remove_from_favorites
     }*/

    companion object {
        private const val PLAYER_START_TIME = "00:00"
    }
}