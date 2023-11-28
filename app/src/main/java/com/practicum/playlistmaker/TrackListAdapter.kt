package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

open class TrackListAdapter(

) : RecyclerView.Adapter<TrackListViewHolder>() {
    open var trackList = ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        return TrackListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.tracklist_element, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

    override fun getItemCount() = trackList.size
}

class ResultTrackListAdapter(
    private val onItemClickListener: OnItemClickListener
) : TrackListAdapter() {
    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener { onItemClickListener.onItemClick(trackList[holder.adapterPosition]) }
    }
}


interface OnItemClickListener {
    fun onItemClick(track: Track)
}