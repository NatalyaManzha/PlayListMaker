package com.practicum.playlistmaker.medialibrary.ui.playlistfullinfo

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.core.view.setPadding
import androidx.lifecycle.lifecycleScope
import com.practicum.playlistmaker.core.ui.BindingFragment
import com.practicum.playlistmaker.databinding.FragmentPlaylistFullInfoBinding
import com.practicum.playlistmaker.medialibrary.ui.models.PlaylistFIUiEvent
import com.practicum.playlistmaker.medialibrary.ui.models.PlaylistFIUiState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates

class PlaylistFIFragment : BindingFragment<FragmentPlaylistFullInfoBinding>() {

    private val viewModel: PlaylistFIViewModel by viewModel()
    private var playlistID by Delegates.notNull<Long>()


    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistFullInfoBinding {
        return FragmentPlaylistFullInfoBinding.inflate(inflater, container, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        playlistID = requireArguments().getLong(PLAYLIST_ID, 0)
        viewModel.onUiEvent(PlaylistFIUiEvent.OnCreateView(playlistID))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeOnViewModel()
    }

    private fun subscribeOnViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { renderState(it) }
        }
    }

    private fun renderState(state: PlaylistFIUiState) {
        setImage(state.iconUri)
        with(binding) {
            playlistFIName.text = state.name
            playlistFIDescription.text = state.description
            playlistFICount.text = state.count
            playlistFIMinutes.text = state.duration

        }
    }

    private fun setImage(uri: Uri?) {
        if (uri != null) {
            with(binding.playlistFIIcon) {
                setPadding(0)
                scaleType = ImageView.ScaleType.CENTER_CROP
                setImageURI(uri)
            }
        }
    }

    companion object {
        private const val PLAYLIST_ID = "playlist_id"

        fun createArgs(playlistID: Long): Bundle =
            bundleOf(PLAYLIST_ID to playlistID)
    }
}