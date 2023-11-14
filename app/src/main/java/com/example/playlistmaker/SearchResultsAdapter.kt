package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchResultsAdapter(private val searchResults: ArrayList<Track>) :
    RecyclerView.Adapter<SearchResultsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_result_track, parent, false)
        return SearchResultsViewHolder(view)
    }

    override fun getItemCount(): Int = searchResults.size

    override fun onBindViewHolder(holder: SearchResultsViewHolder, position: Int) {
        holder.bind(searchResults[position])
    }

}