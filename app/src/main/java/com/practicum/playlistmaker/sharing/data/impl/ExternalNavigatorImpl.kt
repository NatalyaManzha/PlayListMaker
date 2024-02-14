package com.practicum.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator

class ExternalNavigatorImpl(private val appContext: Context) : ExternalNavigator {
    override fun shareLink(url: String) {
        val intent = Intent()
        intent.apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }
        val intentWrapper = Intent.createChooser(intent, null)
        startNewTask(intentWrapper)
    }

    override fun openUserAgreement(url: String) {
        Intent().apply {
            action = android.content.Intent.ACTION_VIEW
            data = android.net.Uri.parse(url)
            startNewTask(this)
        }
    }

    override fun writeToSupport(subject: String, message: String, email: String) {
        Intent().apply {
            action = android.content.Intent.ACTION_SENDTO
            data = android.net.Uri.parse("mailto:")
            putExtra(android.content.Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(android.content.Intent.EXTRA_TEXT, message)
            putExtra(android.content.Intent.EXTRA_SUBJECT, subject)
            startNewTask(this)
        }
    }
    private fun startNewTask(intent: Intent){
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        appContext.startActivity(intent)
    }
}
