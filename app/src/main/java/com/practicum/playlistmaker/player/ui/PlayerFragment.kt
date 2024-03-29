package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.ui.BindingFragment
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.player.domain.models.MediaPlayerState
import com.practicum.playlistmaker.player.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerFragment : BindingFragment<FragmentPlayerBinding>() {

    private lateinit var track: Track
    private val viewModel: PlayerViewModel by viewModel()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayerBinding {
        return FragmentPlayerBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        track = requireArguments().getSerializable(TRACK_TO_PLAY) as Track
        viewModel.run {
            observePlayerState().observe(viewLifecycleOwner) {
                renderPlayerState(it)
            }
            observeCurrentPosition().observe(viewLifecycleOwner) {
                renderCurrentPosition(it)
            }
            observeFavorites().observe(viewLifecycleOwner) {
                renderFavorites(it)
            }
            checkFavorites(track.trackId)
        }
        with(binding) {
            backButton.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
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
            .transform(RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.radius_8dp)))
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
        Toast.makeText(
            requireContext(),
            getString(R.string.player_error_message),
            Toast.LENGTH_LONG
        )
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

        fun createArgs(track: Track): Bundle =
            bundleOf(TRACK_TO_PLAY to track)
    }
}