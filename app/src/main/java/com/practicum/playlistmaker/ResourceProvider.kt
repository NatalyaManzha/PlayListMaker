package com.practicum.playlistmaker

import android.app.Application
import android.content.res.Configuration

object ResourceProvider {
    private lateinit var application: Application


    fun setApplication(application: Application) {
        this.application = application
    }

    fun getString(resId: Int): String {
        return application.getString(resId)
    }
    fun isAppDarkThemeEnabled(): Boolean =
        (application.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES)
    }
