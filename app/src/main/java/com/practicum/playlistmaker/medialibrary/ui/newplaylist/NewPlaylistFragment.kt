package com.practicum.playlistmaker.medialibrary.ui.newplaylist

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.ui.BindingFragment
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.practicum.playlistmaker.medialibrary.ui.models.NewPlaylistUiEvent
import com.practicum.playlistmaker.medialibrary.ui.models.NewPlaylistUiState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : BindingFragment<FragmentNewPlaylistBinding>() {

    private val viewModel: NewPlaylistViewModel by viewModel()
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            showDialog()
        }
    }
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private var dialogEnabled = false

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNewPlaylistBinding {
        return FragmentNewPlaylistBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)
        registerPickMedia()
        setOnTextChangedListeners()
        setOnClickListeners()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiStateFlow.collect {
                render(it)
            }
        }
    }

    private fun registerPickMedia() {
        pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    viewModel.onUiEvent(NewPlaylistUiEvent.ImageChanged(uri))
                } else {
                    showToast(getString(R.string.no_image))
                }
            }
    }

    private fun setNewImage(uri: Uri) {
        binding.newPlaylistIcon.scaleType = ImageView.ScaleType.CENTER_CROP
        binding.newPlaylistIcon.setImageURI(uri)
    }

    private fun render(state: NewPlaylistUiState) {
        if (state.uri != null) setNewImage(state.uri)
        dialogEnabled = state.showDialog
        saveEnabled(state.saveEnabled)
        if (state.saveCompletedSuccessfully != null && state.playlistName != null)
            onSaveComplete(state.saveCompletedSuccessfully, state.playlistName)
    }

    private fun showDialog() {
        if (dialogEnabled) {
            MaterialAlertDialogBuilder(requireActivity())
                .setTitle(getString(R.string.dialog_title))
                .setMessage(getString(R.string.dialog_message))
                .setNeutralButton(getString(R.string.cansel)) { dialog, which ->
                    onBackPressedCallback.isEnabled = true
                }.setPositiveButton(getString(R.string.finish)) { dialog, which ->
                    closeFragment()
                }.show()
        } else closeFragment()
    }

    private fun saveEnabled(enabled: Boolean) {
        binding.createButton.isEnabled = enabled
    }

    private fun setOnClickListeners() {
        with(binding) {
            newPlaylistIcon.setOnClickListener {
                tryToGetImage()
            }
            createButton.setOnClickListener {
                viewModel.onUiEvent(NewPlaylistUiEvent.OnCreateButtonClick)
            }
            newPlaylistBackButton.setOnClickListener {
                showDialog()
            }
        }
    }

    private fun tryToGetImage() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun onSaveComplete(saveResult: Boolean, playlistName: String) {
        val message = if (saveResult) getString(R.string.playlist)+" n\"${playlistName}n\" "+getString(R.string.save_success)
        else getString(R.string.save_failure)+" n\"${playlistName}n\""
        showToast(message)
        closeFragment()
    }

    private fun showToast(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_LONG
        )
            .show()
    }

    private fun setOnTextChangedListeners() {
        binding.nameEditText.onTextChange(true)
        binding.descriptionEditText.onTextChange(false)
    }

    private fun TextInputEditText.onTextChange(flag: Boolean) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onUiEvent(NewPlaylistUiEvent.EditText(flag, s))
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun closeFragment() {
        onBackPressedCallback.isEnabled = false
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }
}