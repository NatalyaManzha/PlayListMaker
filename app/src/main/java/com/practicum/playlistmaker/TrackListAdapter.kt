package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackListAdapter() : RecyclerView.Adapter<TrackListViewHolder>() {

    lateinit var trackList: MutableList<Track>
    var onItemClickListener: ((Track) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        return TrackListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.tracklist_element, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(trackList[position])
        if (onItemClickListener != null) holder.itemView.setOnClickListener { _ ->
            onItemClickListener?.let { it -> it(trackList[holder.adapterPosition]) }
        }
    }

    override fun getItemCount() = trackList.size
}
