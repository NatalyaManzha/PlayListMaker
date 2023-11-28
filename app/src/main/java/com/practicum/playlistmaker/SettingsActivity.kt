package com.practicum.playlistmaker

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var switcher: SwitchMaterial
    private lateinit var shareButton: ImageView
    private lateinit var supportButton: ImageView
    private lateinit var userAgreementButton: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        toolbar = findViewById<Toolbar>(R.id.settings_toolbar)
        switcher = findViewById<SwitchMaterial>(R.id.switcher)
        shareButton = findViewById<ImageView>(R.id.shareButton)
        supportButton = findViewById<ImageView>(R.id.support_button)
        userAgreementButton = findViewById<ImageView>(R.id.user_agreement)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        turnSwitcher()
        switcher.setOnCheckedChangeListener { _, checked ->
            val sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
            if (checked) {
                (applicationContext as PlaylistMakerApp).switchTheme(darkThemeEnabled = true)
                sharedPrefs.edit()
                    .putString(NIGHT_THEME_ENABLED, "true")
                    .apply()
            } else {
                (applicationContext as PlaylistMakerApp).switchTheme(darkThemeEnabled = false)
                sharedPrefs.edit()
                    .putString(NIGHT_THEME_ENABLED, "false")
                    .apply()
            }
        }

        shareButton.setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.android_developer_course_link))
                type = "text/plain"
                startActivity(Intent.createChooser(this, null))
            }
        }

        supportButton.setOnClickListener {
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

        userAgreementButton.setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(getString(R.string.offer_link))
                startActivity(this)
            }
        }
    }

    fun turnSwitcher() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_YES -> {
                switcher.isChecked = true
            }

            Configuration.UI_MODE_NIGHT_NO -> {
                switcher.isChecked = false
            }
        }
    }
}