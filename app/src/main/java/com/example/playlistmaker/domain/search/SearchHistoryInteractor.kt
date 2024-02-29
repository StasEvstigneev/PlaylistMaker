package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.models.Track

interface SearchHistoryInteractor {

    fun getSearchHistory(): ArrayList<Track>

    fun addElementToSearchHistory(newTrack: Track)

    fun clearSearchHistory()

    fun selectTrackForPlayer(selectedTrack: Track)

    fun receiveTackInPlayer(): Track


}