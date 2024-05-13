package com.practicum.playlistmaker.player.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.ui.BindingFragment
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview
import com.practicum.playlistmaker.player.domain.models.MediaPlayerState
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.player.ui.models.PlayerUiEvent
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerFragment : BindingFragment<FragmentPlayerBinding>() {

    private lateinit var track: Track
    private val viewModel: PlayerViewModel by viewModel()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var playlistsAdapter: PlaylistsBSAdapter

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayerBinding {
        return FragmentPlayerBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderInitialValues()
        viewModel.onUiEvent(PlayerUiEvent.OnViewCreated(track))
        setOnClickListeners()
        setBottomSheetBehavior()
        subscribeOnViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onUiEvent(PlayerUiEvent.OnResume)
    }

    override fun onPause() {
        super.onPause()
        viewModel.onUiEvent(PlayerUiEvent.OnPause)
    }

    private fun setBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.overlay.visibility =
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> View.GONE
                        else -> View.VISIBLE
                    }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset
            }
        })
        playlistsAdapter = PlaylistsBSAdapter { playlist ->
            viewModel.onUiEvent(PlayerUiEvent.AddTrackToPlaylist(playlist.id, playlist.name))
        }.apply {
            playlists = emptyList()
        }
        binding.playlistsBottomSheetRW.adapter = playlistsAdapter
    }

    private fun setOnClickListeners() {
        with(binding) {
            backButton.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            playControlButton.setOnClickListener {
                viewModel.onUiEvent(PlayerUiEvent.PlayControlButtonClick)
            }
            addToFavoritesButton.setOnClickListener {
                viewModel.onUiEvent(PlayerUiEvent.AddToFavoritesButtonClick)
            }
            addToPlaylistButton.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
            newPlaylistButton.setOnClickListener {
                findNavController().navigate(R.id.action_playerFragment_to_newPlaylistFragment)
            }
        }
    }

    private fun subscribeOnViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect {
                with(it) {
                    renderFavorites(isInFavorites)
                    renderCurrentPosition(currentPosition)
                    renderPlayerState(playerState)
                    updatePlaylistsData(playlists)
                    if (saveTrackSuccess != null
                        && playlistName != null
                    ) onAddTrackToPlaylistResult(
                        saveTrackSuccess,
                        playlistName
                    )
                }
            }
        }
    }

    private fun onAddTrackToPlaylistResult(isSuccessful: Boolean, playlistName: String) {
        var message = ""
        if (isSuccessful) {
            message = "${getString(R.string.add_track_to_playlist_success)} $playlistName"
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        } else message = "${getString(R.string.add_track_to_playlist_added_yet)} $playlistName"
        showToast(message)
    }

    private fun updatePlaylistsData(playlists: List<PlaylistPreview>) {
        playlistsAdapter.playlists = playlists
        binding.playlistsBottomSheetRW.adapter?.notifyDataSetChanged()
    }

    private fun renderInitialValues() {
        track =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requireArguments().getSerializable(TRACK_TO_PLAY, Track::class.java) as Track
            } else {
                requireArguments().getSerializable(TRACK_TO_PLAY) as Track
            }
        with(binding) {
            trackNameTV.text = track.trackName
            artistNameTV.text = track.artistName
            durationTV.text = track.trackTimeMinSec
            albumTV.text = track.collectionName
            yearTV.text = track.releaseYear
            genreTV.text = track.primaryGenreName
            countryTV.text = track.country
        }
        Glide.with(this)
            .load(track.artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.radius_8dp)))
            .into(binding.coverArtwork)
    }

    private fun renderPlayerState(state: MediaPlayerState) {
        with(binding.playControlButton) {
            when (state) {
                MediaPlayerState.PREPARED -> isEnabled = true
                MediaPlayerState.PLAYBACK_COMPLETE,
                MediaPlayerState.PAUSED,
                MediaPlayerState.DEFAULT -> setImageResource(R.drawable.button_play)

                MediaPlayerState.ERROR -> showToast(getString(R.string.player_error_message))
                MediaPlayerState.PLAYING -> setImageResource(R.drawable.button_pause)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_LONG
        )
            .show()
    }

    private fun renderCurrentPosition(currentPosition: String) {
        binding.trackPlaytimeTV.text = currentPosition
    }

    private fun renderFavorites(isInFavorites: Boolean) {
        val resource =
            if (isInFavorites) R.drawable.remove_from_favorites else R.drawable.add_to_favorites
        binding.addToFavoritesButton.setImageResource(resource)
    }

    companion object {
        private const val TRACK_TO_PLAY = "track_to_play"

        fun createArgs(track: Track): Bundle =
            bundleOf(TRACK_TO_PLAY to track)
    }
}