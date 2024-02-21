package com.practicum.playlistmaker.settings.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity() {
    private val viewModel: SettingsViewModel by viewModel()
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.observeAppTheme().observe(this) {
            turnSwitcher(it)
        }
        with(binding) {
            settingsToolbar.setNavigationOnClickListener {
                onBackPressed()
            }
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

    companion object {
        fun show(context: Context) {
            val intent = Intent(context, SettingsActivity::class.java)
            context.startActivity(intent)
        }
    }
}