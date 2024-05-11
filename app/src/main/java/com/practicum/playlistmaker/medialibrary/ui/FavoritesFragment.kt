package com.practicum.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.ui.BindingFragment
import com.practicum.playlistmaker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmaker.medialibrary.ui.models.FavoritesUiState
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.search.ui.TrackListAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : BindingFragment<FragmentFavoritesBinding>() {

    private lateinit var trackListAdapter: TrackListAdapter
    private val viewModel: FavoritesViewModel by viewModel()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritesBinding {
        return FragmentFavoritesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackListAdapter = TrackListAdapter { track ->
            goToPlayer(track)
        }.apply {
            trackList = emptyList()
        }
        binding.favoritesRV.adapter = trackListAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiStateFlow.collect {
                render(it)
            }
        }
    }

    private fun render(state: FavoritesUiState) {
        when (state) {
            FavoritesUiState.Default -> showDefaultState()
            is FavoritesUiState.ShowFavorites -> showFavorites(state.tracklist)
            FavoritesUiState.Placeholder -> showPlaceholder()
        }
    }

    private fun showPlaceholder() {
        binding.noFavoritesPlaceholder.isVisible = true
        binding.favoritesRV.isVisible = false
    }

    private fun showDefaultState() {
        binding.noFavoritesPlaceholder.isVisible = false
        binding.favoritesRV.isVisible = false
    }

    private fun showFavorites(trackList: List<Track>) {
        with(binding) {
            noFavoritesPlaceholder.isVisible = false
            trackListAdapter.trackList = trackList
            favoritesRV.apply {
                adapter?.notifyDataSetChanged()
                isVisible = true
            }
        }
    }

    private fun goToPlayer(track: Track) {
        findNavController().navigate(
            R.id.actionMediaLibraryFragmentToPlayerFragment,
            PlayerFragment.createArgs(track)
        )
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }
}