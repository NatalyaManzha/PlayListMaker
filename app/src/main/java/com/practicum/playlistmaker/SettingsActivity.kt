package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.settings_toolbar)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val shareButton = findViewById<ImageView>(R.id.shareButton)
        shareButton.setOnClickListener {

            val intent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.android_developer_course_link))
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(intent, null)
            startActivity(shareIntent)
        }

        val supportButton = findViewById<ImageView>(R.id.support_button)
        supportButton.setOnClickListener {
            val subject = getString(R.string.support_message_subject)
            val message = getString(R.string.thanks)
            val intent: Intent = Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email)))
                putExtra(Intent.EXTRA_TEXT, message)
                putExtra(Intent.EXTRA_SUBJECT, subject)
            }
            startActivity(intent)
        }

        val userAgreementButton = findViewById<ImageView>(R.id.user_agreement)
        userAgreementButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.offer_link))))
        }
    }
}