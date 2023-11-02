package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackListAdapter(
    private val trackList: ArrayList<Track>
) : RecyclerView.Adapter<TrackListAdapter.TrackListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        return TrackListViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.tracklist_element, parent, false))
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

    override fun getItemCount() = trackList.size

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
            trackDuration.text = track.trackTime
            Glide.with(itemView)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .transform(RoundedCorners(2))
                .fitCenter()
                .into(icon)
        }
    }
}