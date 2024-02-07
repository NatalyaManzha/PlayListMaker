package com.practicum.playlistmaker.search.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.TracklistElementBinding
import com.practicum.playlistmaker.player.domain.models.Track

class TrackListViewHolder(private val binding: TracklistElementBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(track: Track) {
        with(binding) {
            trackName.text = track.trackName
            trackMusician.text = track.artistName
            trackDuration.text = track.trackTimeMinSec
            Glide.with(itemView)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.radius_2dp)))
                .into(trackIcon)
        }
    }
}