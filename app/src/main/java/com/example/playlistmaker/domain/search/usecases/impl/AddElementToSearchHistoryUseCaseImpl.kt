package com.example.playlistmaker.domain.search.usecases.impl

import com.example.playlistmaker.domain.GsonJsonConverter
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.search.usecases.AddElementToSearchHistoryUseCase
import com.example.playlistmaker.domain.search.usecases.GetSearchHistoryUseCase
import com.example.playlistmaker.domain.storage.LocalStorage


class AddElementToSearchHistoryUseCaseImpl(private val localStorage: LocalStorage, private val getSearchHistoryUseCase: GetSearchHistoryUseCase, private val gsonJsonConverter: GsonJsonConverter):
    AddElementToSearchHistoryUseCase {

    override fun execute(key:String, newTrack: Track, itemsLimit: Int) {
        val searchHistory = getSearchHistoryUseCase.execute(key)
        val iterator = searchHistory.iterator()
        while (iterator.hasNext()) {
            val track = iterator.next()
            if (track.trackId == newTrack.trackId) {
                iterator.remove()
            }
        }

        if (searchHistory.size >= itemsLimit) {
            searchHistory.removeFirst()
        }

        searchHistory.add(newTrack)
        val json = gsonJsonConverter.getJsonFromArrayList(searchHistory)
        localStorage.addStringData(key, json)
    }
}