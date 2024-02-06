package com.example.playlistmaker.domain.usecase.searchhistory

import com.example.playlistmaker.domain.Constants.SEARCH_HISTORY_KEY
import com.example.playlistmaker.domain.GsonJsonConverter
import com.example.playlistmaker.domain.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track


class GetSearchHistoryUseCaseUseCaseImpl(private val searchHistoryRepository: SearchHistoryRepository, private val gsonJsonConverter: GsonJsonConverter):
    GetSearchHistoryUseCase {

    override fun execute(): ArrayList<Track> {
        val json = searchHistoryRepository.sharedPreferences
            .getString(SEARCH_HISTORY_KEY, null) ?: return ArrayList()
        return gsonJsonConverter.getTrackListFromJson(json)
    }
}