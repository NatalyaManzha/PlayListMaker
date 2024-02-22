package com.practicum.playlistmaker.utils

import android.content.Context
import android.content.res.Configuration

class ResourceProvider(
    private val appContext: Context
) {
    fun getString(resId: Int): String {
        return appContext.getString(resId)
    }

    fun isAppDarkThemeEnabled(): Boolean =
        (appContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES)
}