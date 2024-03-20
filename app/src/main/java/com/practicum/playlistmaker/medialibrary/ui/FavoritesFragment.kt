package com.practicum.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.practicum.playlistmaker.core.ui.BindingFragment
import com.practicum.playlistmaker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmaker.medialibrary.ui.models.FragmentUiState
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : BindingFragment<FragmentFavoritesBinding>() {
    private val viewModel: FavoritesViewModel by viewModel()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritesBinding {
        return FragmentFavoritesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: FragmentUiState) {
        when (state) {
            is FragmentUiState.Default -> showDefaultState()
        }
    }

    private fun showDefaultState() {
        binding.noFavoritesPlaceholder.isVisible = true
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }
}