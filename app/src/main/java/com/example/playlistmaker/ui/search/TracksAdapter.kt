package com.example.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.models.Track

class TracksAdapter(
    var list: ArrayList<Track>,
    private val clickListener: OnTrackClickListener
) : RecyclerView.Adapter<TracksViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_track, parent, false)
        return TracksViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            clickListener.onTrackClick(list[position])
        }

    }

    fun interface OnTrackClickListener {
        fun onTrackClick(track: Track)
    }


}