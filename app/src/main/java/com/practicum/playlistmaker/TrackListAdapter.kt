package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackListAdapter (
    var onItemClickListener: ((Track) -> Unit)
): RecyclerView.Adapter<TrackListViewHolder>() {

    lateinit var trackList: MutableList<Track>


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        return TrackListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.tracklist_element, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener { _ ->
            onItemClickListener.invoke(trackList[holder.adapterPosition])
        }
    }

    override fun getItemCount() = trackList.size
}
