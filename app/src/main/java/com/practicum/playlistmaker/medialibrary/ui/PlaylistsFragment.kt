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
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview
import com.practicum.playlistmaker.medialibrary.ui.models.PlaylistsUiState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : BindingFragment<FragmentPlaylistsBinding>() {

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
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_newPlaylistFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiStateFlow.collect {
                render(it)
            }
        }
    }

    private fun render(state: PlaylistsUiState) {
        when (state) {
            is PlaylistsUiState.Default -> showDefaultState()
            is PlaylistsUiState.ShowPlaylists -> showPlaylistsState(state.playlists)
        }
    }

    private fun showPlaylistsState(playlists: List<PlaylistPreview>) {
        //TODO
    }

    private fun showDefaultState() {
        binding.noPlaylistsPlaceholder.isVisible = true
        binding.addNewPlaylistButton.isEnabled = true
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}