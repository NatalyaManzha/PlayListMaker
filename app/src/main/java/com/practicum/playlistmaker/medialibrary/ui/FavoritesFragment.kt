package com.practicum.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.ui.BindingFragment
import com.practicum.playlistmaker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmaker.medialibrary.ui.models.FavoritesUiState
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.search.ui.TrackListAdapter
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

        binding.favoritesRV.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = trackListAdapter
        }
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: FavoritesUiState) {
        when (state) {
            is FavoritesUiState.Default -> showDefaultState()
            is FavoritesUiState.ShowFavorites -> showFavoritesState(state.tracklist)
        }
    }

    private fun showDefaultState() {
        binding.noFavoritesPlaceholder.isVisible = true
        binding.favoritesRV.isVisible = false
    }

    private fun showFavoritesState(trackList: List<Track>) {
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