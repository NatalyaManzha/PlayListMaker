package com.practicum.playlistmaker.medialibrary.ui.playlistfullinfo.utils

import com.practicum.playlistmaker.player.domain.models.Track

fun countDuration(trackList: List<Track>): String {
    var totalMinutes: Int
    val totalSeconds = trackList.sumOf { track ->
        val (minutes, seconds) = track.trackTimeMinSec.split(":".toRegex())
        val minutesCount = minutes.toInt()
        val secondsCount = seconds.toInt()
        minutesCount * 60 + secondsCount
    }
    totalMinutes = totalSeconds / 60
    if (totalSeconds % 60 > 30) totalMinutes += 1
    return chooseCaseMinutes(totalMinutes)
}

fun chooseCaseMinutes(minutes: Int): String {
    val str =
        if ((minutes % 100) in (11..14)) "минут"
        else when (minutes % 10) {
            1 -> "минута"
            0, 5, 6, 7, 8, 9 -> "минут"
            else -> "минуты"
        }
    return "$minutes $str"
}