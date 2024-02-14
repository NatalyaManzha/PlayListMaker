package com.practicum.playlistmaker.player.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.domain.models.MediaPlayerState
import com.practicum.playlistmaker.player.domain.models.Track


class PlayerActivity : AppCompatActivity() {

    private lateinit var track: Track
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        track = intent.getSerializableExtra(TRACK_TO_PLAY) as Track
        viewModel = ViewModelProvider(this)[PlayerViewModel::class.java]
        viewModel.run {
            val owner = this@PlayerActivity
            observePlayerState().observe(owner) {
                renderPlayerState(it)
            }
            observeCurrentPosition().observe(owner) {
                renderCurrentPosition(it)
            }
            observeFavorites().observe(owner) {
                renderFavorites(it)
            }
            checkFavorites(track.trackId)
        }

        with(binding) {
            backButton.setOnClickListener {
                finish()
            }
            trackNameTV.text = track.trackName
            artistNameTV.text = track.artistName
            durationTV.text = track.trackTimeMinSec
            albumTV.text = track.collectionName
            yearTV.text = track.releaseYear
            genreTV.text = track.primaryGenreName
            countryTV.text = track.country
            playControlButton.setOnClickListener {
                viewModel.playbackControl()
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

        viewModel.preparePlayer(track.previewUrl)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onPlayerActivityOnResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPlayerActivityPause()
    }

    private fun renderPlayerState(state: MediaPlayerState) {
        with(binding.playControlButton) {
            when (state) {
                MediaPlayerState.PREPARED -> isEnabled = true
                MediaPlayerState.PLAYBACK_COMPLETE,
                MediaPlayerState.PAUSED,
                MediaPlayerState.DEFAULT -> setImageResource(R.drawable.button_play)
                MediaPlayerState.ERROR -> showToast()
                MediaPlayerState.PLAYING -> setImageResource(R.drawable.button_pause)
            }
        }
    }
    private fun showToast() {
        Toast.makeText(this, getString(R.string.player_error_message), Toast.LENGTH_LONG)
            .show()
    }

    private fun renderCurrentPosition(currentPosition: String) {
        binding.trackPlaytimeTV.text = currentPosition
    }

    private fun renderFavorites(isInFavorites: Boolean) {
        val resourse =
            if (isInFavorites) R.drawable.remove_from_favorites else R.drawable.add_to_favorites
        binding.addToFavoritesButton.setImageResource(resourse)
    }

    companion object {
        private const val TRACK_TO_PLAY = "track_to_play"
        fun show(context: Context, track: Track) {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(TRACK_TO_PLAY, track)
            context.startActivity(intent)
        }
    }
}