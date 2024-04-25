package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.practicum.playlistmaker.core.ui.BindingFragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.settings.ui.models.SettingsUiEvent
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : BindingFragment<FragmentSettingsBinding>() {

    private val viewModel: SettingsViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.darkThemeEnabledFlow.collect {
                turnSwitcher(it)
            }
        }
        with(binding) {
            switcher.setOnCheckedChangeListener { _, checked ->
                viewModel.onUiEvent(SettingsUiEvent.SwitcherChecked(checked))
            }
            shareButton.setOnClickListener {
                viewModel.onUiEvent(SettingsUiEvent.ShareButtonClick)
            }
            supportButton.setOnClickListener {
                viewModel.onUiEvent(SettingsUiEvent.SupportButtonClick)
            }
            userAgreementButton.setOnClickListener {
                viewModel.onUiEvent(SettingsUiEvent.UserAgreementButtonClick)
            }
        }
    }

    private fun turnSwitcher(isChecked: Boolean) {
        binding.switcher.isChecked = isChecked
    }
}