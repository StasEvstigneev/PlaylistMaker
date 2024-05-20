package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.models.Track

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {
    override fun getSearchHistory(): ArrayList<Track> {
        return repository.getSearchHistory()
    }

    override fun addElementToSearchHistory(newTrack: Track) {
        repository.addElementToSearchHistory(newTrack)
    }

    override fun clearSearchHistory() {
        repository.clearSearchHistory()
    }

}