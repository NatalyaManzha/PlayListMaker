package com.practicum.playlistmaker.player.data

import android.icu.text.SimpleDateFormat
import java.util.Locale

object TimeFormatter {
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    fun format(currentPosition: Int): String {
        return dateFormat.format(currentPosition)
    }
}