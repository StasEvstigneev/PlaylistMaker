package com.example.playlistmaker.ui.search.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.models.Track

class SearchResultsAdapter(private var searchResults: ArrayList<Track>, private val clickListener: OnTrackClickListener) :
    RecyclerView.Adapter<SearchResultsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_result_track, parent, false)
        return SearchResultsViewHolder(view)
    }

    override fun getItemCount(): Int = searchResults.size

    override fun onBindViewHolder(holder: SearchResultsViewHolder, position: Int) {
        holder.bind(searchResults[position])
        holder.itemView.setOnClickListener {
            clickListener.onTrackClick(searchResults[position])
        }
    }

    fun interface OnTrackClickListener {
        fun onTrackClick(track: Track)
    }

    fun setItems(newList: ArrayList<Track>) {
        val oldList = searchResults
        val diffUtil = object: DiffUtil.Callback(){

            override fun getOldListSize(): Int {
                return oldList.size
            }

            override fun getNewListSize(): Int {
                return newList.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].trackId == newList[newItemPosition].trackId
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == oldList[newItemPosition]
            }

        }

        val diffResult = DiffUtil.calculateDiff(diffUtil)
        searchResults = newList
        diffResult.dispatchUpdatesTo(this)


    }




}