package com.practicum.playlistmaker.medialibrary.ui

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.ui.BindingFragment
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.practicum.playlistmaker.medialibrary.ui.models.NewPlaylistUiEvent
import com.practicum.playlistmaker.medialibrary.ui.models.NewPlaylistUiState2
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : BindingFragment<FragmentNewPlaylistBinding>() {

    private val viewModel: NewPlaylistViewModel by viewModel()
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            showDialog()
        }
    }.also {
        Log.d("QQQ", "OnBackPressedCallback ${it?.hashCode()}")
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
        activity?.also {
            Log.d("QQQ", "${this?.hashCode()}")
        }?.onBackPressedDispatcher?.also {
            Log.d("QQQ", "${it?.hashCode()}")
        }?.addCallback(onBackPressedCallback).also {
            Log.d("QQQ", "колбек добавлен")
        }
        registerPickMedia()
        setOnTextChangedListeners()
        setOnClickListeners()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiStateFlow.collect {
                Log.d("QQQ", "$it")
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
                    showToast()
                }
            }
    }

    private fun setNewImage(uri: Uri) {
        binding.newPlaylistIcon.scaleType = ImageView.ScaleType.CENTER_CROP
        binding.newPlaylistIcon.setImageURI(uri)
    }

    private fun render(state: NewPlaylistUiState2) {
        if (state.uri != null) setNewImage(state.uri)
        dialogEnabled = state.showDialog
        Log.d("QQQ", "dialogEnabled = $dialogEnabled")
        saveEnabled(state.saveEnabled)

    }

    private fun showDialog() {
        Log.d("QQQ", "showDialog(): dialogEnabled = $dialogEnabled")
        if (dialogEnabled) {
            MaterialAlertDialogBuilder(requireActivity())
                .setTitle(getString(R.string.dialog_title))
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
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            createButton.setOnClickListener {
                viewModel.onUiEvent(NewPlaylistUiEvent.OnCreateButtonClick)
                closeFragment()
            }
            newPlaylistBackButton.setOnClickListener {
                showDialog()
            }
        }
    }

    private fun showToast() {
        Toast.makeText(
            requireContext(),
            getString(R.string.no_image),
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

    override fun onDestroy() {
        super.onDestroy()
        //это уже даже излишне, ничего не меняется
        onBackPressedCallback.remove()
    }
}