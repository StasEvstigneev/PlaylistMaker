package com.example.playlistmaker.ui.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.search.TracksViewHolder

class PlaylistAdapter(
    var list: ArrayList<Track>,
    private val recyclerViewClickInterface: RecyclerViewClickInterface
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
            recyclerViewClickInterface.onItemCLick(list[position])
        }

        holder.itemView.setOnLongClickListener {
            recyclerViewClickInterface.onLongItemClick(list[position])
            true
        }

    }

}