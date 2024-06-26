package com.practicum.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator

class ExternalNavigatorImpl(private val appContext: Context) : ExternalNavigator {
    override fun shareData(data: String) {
        val intent = Intent()
        intent.apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, data)
            type = "text/plain"
        }
        val intentWrapper = Intent.createChooser(intent, null)
        startNewTask(intentWrapper)
    }

    override fun openUserAgreement(url: String) {
        Intent().apply {
            action = Intent.ACTION_VIEW
            data = android.net.Uri.parse(url)
            startNewTask(this)
        }
    }

    override fun writeToSupport(subject: String, message: String, email: String) {
        Intent().apply {
            action = Intent.ACTION_SENDTO
            data = android.net.Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_TEXT, message)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            startNewTask(this)
        }
    }

    private fun startNewTask(intent: Intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        appContext.startActivity(intent)
    }
}
