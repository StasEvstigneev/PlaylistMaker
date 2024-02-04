package com.example.playlistmaker.domain.usecase.searchhistory

import com.example.playlistmaker.domain.Constants.SEARCH_HISTORY_ITEMS_LIMIT
import com.example.playlistmaker.domain.Constants.SEARCH_HISTORY_KEY
import com.example.playlistmaker.domain.GsonJsonConverter
import com.example.playlistmaker.domain.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track


class AddElementToSearchHistoryUseCase(private val searchHistoryRepository: SearchHistoryRepository, private val getSearchHistoryUseCase: GetSearchHistory, private val gsonJsonConverter: GsonJsonConverter):
    AddElementToSearchHistory {

    override fun execute(newTrack: Track) {
        val searchHistory = getSearchHistoryUseCase.execute()
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
        searchHistoryRepository.sharedPreferences.edit().putString(SEARCH_HISTORY_KEY, json).apply()
    }
}