package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.domain.GsonJsonConverter
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.storage.LocalStorage


class SearchHistoryRepositoryImpl(
    private val localStorage: LocalStorage,
    private val gsonJsonConverter: GsonJsonConverter
): SearchHistoryRepository {


    override fun getSearchHistory(): ArrayList<Track> {
        val json = localStorage.getSavedStringData(SEARCH_HISTORY_KEY)
        return gsonJsonConverter.getTrackListFromJson(json)

    }

    override fun addElementToSearchHistory(newTrack: Track) {
        val searchHistory = getSearchHistory()
        val iterator = searchHistory.iterator()
        while (iterator.hasNext()) {
            val track = iterator.next()
            if (track.trackId == newTrack.trackId) {
                iterator.remove()
            }
        }

        if (searchHistory.size >= SEARCH_HISTORY_ITEMS_LIMIT) {
            searchHistory.removeFirst()
        }

        searchHistory.add(newTrack)
        val json = gsonJsonConverter.getJsonFromArrayList(searchHistory)
        localStorage.addStringData(SEARCH_HISTORY_KEY, json)

    }

    override fun clearSearchHistory() {
        localStorage.clearData()
    }


    companion object {

        private const val SEARCH_HISTORY_KEY = "SearchHistoryKey"
        private const val SEARCH_HISTORY_ITEMS_LIMIT: Int = 10

    }


}