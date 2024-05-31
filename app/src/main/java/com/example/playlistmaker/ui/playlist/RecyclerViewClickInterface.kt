package com.example.playlistmaker.ui.playlist

import com.example.playlistmaker.domain.search.models.Track

interface RecyclerViewClickInterface {

    fun onItemCLick(track: Track)
    fun onLongItemClick(track: Track)


}