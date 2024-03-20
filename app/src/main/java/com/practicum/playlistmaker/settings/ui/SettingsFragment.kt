package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.practicum.playlistmaker.core.ui.BindingFragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
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
        viewModel.observeAppTheme().observe(viewLifecycleOwner) {
            turnSwitcher(it)
        }
        with(binding) {
            switcher.setOnCheckedChangeListener { _, checked ->
                viewModel.applyDarkTheme(checked)
            }
            shareButton.setOnClickListener {
                viewModel.shareLink()
            }
            supportButton.setOnClickListener {
                viewModel.writeToSupport()
            }
            userAgreementButton.setOnClickListener {
                viewModel.goToUserAgreement()
            }
        }
    }

    private fun turnSwitcher(isChecked: Boolean) {
        binding.switcher.isChecked = isChecked
    }
}