package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val icon: ImageView
    private val trackName: TextView
    private val trackMusician: TextView
    private val trackDuration: TextView


    init {
        trackName = itemView.findViewById(R.id.track_name)
        trackMusician = itemView.findViewById(R.id.track_musician)
        trackDuration = itemView.findViewById(R.id.track_duration)
        icon = itemView.findViewById(R.id.track_icon)
    }

    fun bind(track: Track) {
        trackName.text = track.trackName
        trackMusician.text = track.artistName
        trackDuration.text = track.getFormatedTime()
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.radius_2dp)))
            .into(icon)
    }
}