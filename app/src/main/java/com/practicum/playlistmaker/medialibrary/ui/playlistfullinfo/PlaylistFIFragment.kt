package com.practicum.playlistmaker.medialibrary.ui.playlistfullinfo

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.setPadding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.ui.BindingFragment
import com.practicum.playlistmaker.databinding.FragmentPlaylistFullInfoBinding
import com.practicum.playlistmaker.medialibrary.domain.models.EditPlaylist
import com.practicum.playlistmaker.medialibrary.ui.editplaylist.EditPlaylistFragment
import com.practicum.playlistmaker.medialibrary.ui.models.PlaylistFIUiEvent
import com.practicum.playlistmaker.medialibrary.ui.models.PlaylistFIUiState
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.player.ui.PlayerFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates

class PlaylistFIFragment : BindingFragment<FragmentPlaylistFullInfoBinding>() {

    private val viewModel: PlaylistFIViewModel by viewModel()
    private var playlistID by Delegates.notNull<Long>()
    private var iconUri: Uri? = null
    private lateinit var trackListBSAdapter: TrackListBSAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var track: Track? = null


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
        initializeAdapter()
        subscribeOnViewModel()
        setOnClickListeners()
        setBottomSheetBehavior()
    }

    private fun initializeAdapter() {
        trackListBSAdapter = TrackListBSAdapter(
            onItemClickListener = { track ->
                this.track = track
                viewModel.onUiEvent(PlaylistFIUiEvent.OnTrackClick(track.trackId))
            },
            onItemLongClickListener = { track ->
                showTrackDeleteDialog(track.trackId)
                true
            }
        ).apply {
            trackList = emptyList()
        }
        binding.playlistFIBottomSheetRW.adapter = trackListBSAdapter
    }

    private fun showTrackDeleteDialog(trackID: Int) {
        val dialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(getString(R.string.delete_track))
            .setMessage(getString(R.string.delete_track_message))
            .setNeutralButton(getString(R.string.cansel)) { _, _ ->
            }.setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.onUiEvent(PlaylistFIUiEvent.DeleteTrack(trackID))
            }.create()
        dialog.show()
        dialog.requireViewById<Space>(com.google.android.material.R.id.spacer).visibility =
            View.GONE
    }

    private fun showPlaylistDeleteDialog() {
        val dialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(getString(R.string.delete_playlist))
            .setMessage(getString(R.string.delete_playlist_message))
            .setNeutralButton(getString(R.string.cansel)) { _, _ ->
            }.setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.onUiEvent(PlaylistFIUiEvent.DeletePlaylist)
            }.create()
        dialog.show()
        dialog.requireViewById<Space>(com.google.android.material.R.id.spacer).visibility =
            View.GONE
    }

    private fun subscribeOnViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { renderState(it) }
        }
    }

    private fun setOnClickListeners() {
        with(binding) {
            playlistFIBackButton.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            playlistFIShare.setOnClickListener { onShareClick() }
            playlistFIMenu.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            menuShare.setOnClickListener { onShareClick() }
            menuEdit.setOnClickListener {
                goToPlaylistEditor()
            }
            menuDelete.setOnClickListener {
                showPlaylistDeleteDialog()
            }
        }
    }

    private fun onShareClick() {
        if (trackListBSAdapter.trackList.isEmpty()) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            showToast(getString(R.string.empty_playlist))
        } else viewModel.onUiEvent(PlaylistFIUiEvent.SharePlaylist)
    }

    private fun renderState(state: PlaylistFIUiState) {
        state.iconUri?.let { setImage(it) }
        state.tracklist?.let { updateTracksData(it) }
        state.isInFavorites?.let { goToPlayer(it) }
        state.playlistDeleted?.let { onPlaylistDeletion(it) }
        with(binding) {
            playlistFIName.text = state.name
            playlistFIDescription.text = state.description
            playlistFICount.text = state.count
            playlistFIMinutes.text = state.duration
        }
    }

    private fun onPlaylistDeletion(result: Boolean) {
        if (!result) showToast(getString(R.string.delete_failure))
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun showToast(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_LONG
        )
            .show()
    }

    private fun updateTracksData(trackList: List<Track>) {
        trackListBSAdapter.trackList = trackList
        binding.playlistFIBottomSheetRW.adapter?.notifyDataSetChanged()
    }

    private fun setImage(uri: Uri) {
        iconUri = uri
        with(binding.playlistFIIcon) {
            setPadding(0)
            scaleType = ImageView.ScaleType.CENTER_CROP
            setImageURI(uri)
        }
    }

    private fun goToPlaylistEditor() {
        findNavController().navigate(
            R.id.actionPlaylistFullInfoFragmentToEditPlaylistFragment,
            EditPlaylistFragment.createArgs(
                EditPlaylist(
                    id = playlistID,
                    iconUri = iconUri,
                    name = binding.playlistFIName.text.toString(),
                    description = binding.playlistFIDescription.text.toString()
                )
            )
        )
    }

    private fun goToPlayer(isInFavorites: Boolean) {
        val trackToPlay = track!!.copy(
            inFavorite = isInFavorites
        )
        track = null
        findNavController().navigate(
            R.id.actionPlaylistFullInfoFragmentToPlayerFragment,
            PlayerFragment.createArgs(trackToPlay)
        )
    }

    private fun setBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistFIBottomSheetMenu).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.playlistFIOverlay.visibility =
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> View.GONE
                        else -> View.VISIBLE
                    }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val alpha = if (slideOffset < 0.5F) slideOffset + 0.5F else 1F
                binding.playlistFIOverlay.alpha = alpha
            }
        })
    }

    companion object {
        private const val PLAYLIST_ID = "playlist_id"

        fun createArgs(playlistID: Long): Bundle =
            bundleOf(PLAYLIST_ID to playlistID)
    }
}