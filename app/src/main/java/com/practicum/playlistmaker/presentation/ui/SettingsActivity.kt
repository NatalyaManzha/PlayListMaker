package com.practicum.playlistmaker.presentation.ui

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.PlaylistMakerApp


class SettingsActivity : AppCompatActivity() {
    private val saveThemeUseCase by lazy { Creator.provideSaveThemeUseCase() }
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingsToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        turnSwitcher()
        binding.switcher.setOnCheckedChangeListener { _, checked ->
            val darkThemeEnabled = if (checked) true else false
            (applicationContext as PlaylistMakerApp).switchTheme(darkThemeEnabled)
            saveThemeUseCase.execute(darkThemeEnabled)
        }

        binding.shareButton.setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.android_developer_course_link))
                type = "text/plain"
                startActivity(Intent.createChooser(this, null))
            }
        }

        binding.supportButton.setOnClickListener {
            val subject = getString(R.string.support_message_subject)
            val message = getString(R.string.thanks)
            Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email)))
                putExtra(Intent.EXTRA_TEXT, message)
                putExtra(Intent.EXTRA_SUBJECT, subject)
                startActivity(this)
            }
        }

        binding.userAgreementButton.setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(getString(R.string.offer_link))
                startActivity(this)
            }
        }
    }

    fun turnSwitcher() {
        binding.switcher.isChecked =
            (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES)
    }
}