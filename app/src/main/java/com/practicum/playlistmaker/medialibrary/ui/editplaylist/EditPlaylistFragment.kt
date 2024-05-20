package com.practicum.playlistmaker.medialibrary.ui.editplaylist

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.ui.BindingFragment
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.practicum.playlistmaker.medialibrary.domain.models.EditPlaylist
import com.practicum.playlistmaker.medialibrary.ui.models.EditPlaylistUiEvent
import com.practicum.playlistmaker.medialibrary.ui.models.EditPlaylistUiState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : BindingFragment<FragmentNewPlaylistBinding>() {

    private val viewModel: EditPlaylistViewModel by viewModel()
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNewPlaylistBinding {
        return FragmentNewPlaylistBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderInitialValues()
        registerPickMedia()
        setOnTextChangedListeners()
        setOnClickListeners()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiStateFlow.collect {
                render(it)
            }
        }
    }

    private fun renderInitialValues() {
        val playlistInfo =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requireArguments().getSerializable(
                    PLAYLIST_TO_EDIT,
                    EditPlaylist::class.java
                ) as EditPlaylist
            } else {
                requireArguments().getSerializable(PLAYLIST_TO_EDIT) as EditPlaylist
            }
        viewModel.onUiEvent(EditPlaylistUiEvent.InitialValues(playlistInfo))

        with(binding) {
            binding.newPlaylistHeader.text = getString(R.string.edit_playlist)
            binding.createButton.text = getString(R.string.save)
            nameEditText.setText(playlistInfo.name)
            descriptionEditText.setText(playlistInfo.description)
        }
    }

    private fun render(state: EditPlaylistUiState) {
        if (state.uri != null) setNewImage(state.uri)
        saveEnabled(state.saveEnabled)
    }

    private fun saveEnabled(enabled: Boolean) {
        binding.createButton.isEnabled = enabled
    }

    private fun setNewImage(uri: Uri) {
        binding.newPlaylistIcon.scaleType = ImageView.ScaleType.CENTER_CROP
        binding.newPlaylistIcon.setImageURI(uri)
    }

    private fun setOnClickListeners() {
        with(binding) {
            newPlaylistIcon.setOnClickListener {
                tryToGetImage()
            }
            createButton.setOnClickListener {
                viewModel.onUiEvent(EditPlaylistUiEvent.OnSaveButtonClick)
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            newPlaylistBackButton.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun registerPickMedia() {
        pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) viewModel.onUiEvent(EditPlaylistUiEvent.ImageChanged(uri))
            }
    }

    private fun tryToGetImage() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun setOnTextChangedListeners() {
        binding.nameEditText.onTextChange(true)
        binding.descriptionEditText.onTextChange(false)
    }

    private fun TextInputEditText.onTextChange(flag: Boolean) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onUiEvent(EditPlaylistUiEvent.EditText(flag, s))
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    companion object {
        private const val PLAYLIST_TO_EDIT = "playlist_to_edit"

        fun createArgs(playlistInfo: EditPlaylist): Bundle =
            bundleOf(PLAYLIST_TO_EDIT to playlistInfo)
    }
}