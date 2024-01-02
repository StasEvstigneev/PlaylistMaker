package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchResultsAdapter(var searchResults: ArrayList<Track>, val clickListener: OnTrackClickListener) :
    RecyclerView.Adapter<SearchResultsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_result_track, parent, false)
        return SearchResultsViewHolder(view)
    }

    override fun getItemCount(): Int = searchResults.size

    override fun onBindViewHolder(holder: SearchResultsViewHolder, position: Int) {
        holder.bind(searchResults[position])
        holder.itemView.setOnClickListener { clickListener.onTrackClick(searchResults.get(position)) }
    }

    fun interface OnTrackClickListener {
        fun onTrackClick(track: Track)
    }

}