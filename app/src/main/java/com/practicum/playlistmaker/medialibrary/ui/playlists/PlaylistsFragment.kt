package com.practicum.playlistmaker.medialibrary.ui.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.ui.BindingFragment
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview
import com.practicum.playlistmaker.medialibrary.ui.models.PlaylistsUiState
import com.practicum.playlistmaker.medialibrary.ui.playlistfullinfo.PlaylistFIFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : BindingFragment<FragmentPlaylistsBinding>() {

    private lateinit var playlistsAdapter: PlaylistsAdapter
    private val viewModel: PlaylistsViewModel by viewModel()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistsBinding {
        return FragmentPlaylistsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addNewPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.actionMediaLibraryFragmentToNewPlaylistFragment)
        }
        playlistsAdapter = PlaylistsAdapter {
            toPlaylistFullInfo(it.id)
        }
            .apply { playlists = emptyList() }
        binding.playlistsRV.adapter = playlistsAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiStateFlow.collect {
                render(it)
            }
        }
    }

    private fun toPlaylistFullInfo(playlistID: Long) {
        findNavController().navigate(
            R.id.actionMediaLibraryFragmentToPlaylistFullInfoFragment,
            PlaylistFIFragment.createArgs(playlistID)
        )
    }

    private fun render(state: PlaylistsUiState) {
        when (state) {
            PlaylistsUiState.Default -> showDefaultState()
            is PlaylistsUiState.ShowPlaylists -> showPlaylistsState(state.playlists)
            PlaylistsUiState.Placeholder -> showPlaceholder()
        }
    }

    private fun showPlaylistsState(playlists: List<PlaylistPreview>) {
        with(binding) {
            noPlaylistsPlaceholder.isVisible = false
            playlistsAdapter.playlists = playlists
            playlistsRV.apply {
                adapter?.notifyDataSetChanged()
                isVisible = true
            }
        }
    }

    private fun showDefaultState() {
        binding.noPlaylistsPlaceholder.isVisible = false
        binding.playlistsRV.isVisible = false
    }

    private fun showPlaceholder() {
        binding.noPlaylistsPlaceholder.isVisible = true
        binding.playlistsRV.isVisible = false
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}