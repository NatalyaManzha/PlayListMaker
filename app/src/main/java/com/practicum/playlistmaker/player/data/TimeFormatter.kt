package com.practicum.playlistmaker.player.data

import android.icu.text.SimpleDateFormat
import java.util.Locale

class TimeFormatter {
    companion object {
        fun format(currentPosition: Int): String {
            val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
            return dateFormat.format(currentPosition)
        }
    }
}