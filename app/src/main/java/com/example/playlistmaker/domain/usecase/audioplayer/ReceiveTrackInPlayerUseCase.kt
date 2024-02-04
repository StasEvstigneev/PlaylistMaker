package com.example.playlistmaker.domain.usecase.audioplayer

import com.example.playlistmaker.domain.Constants.SELECTED_TRACK_KEY
import com.example.playlistmaker.domain.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.GsonJsonConverter

class ReceiveTrackInPlayerUseCase(private val searchHistoryRepository: SearchHistoryRepository, private val gsonJsonConverter: GsonJsonConverter) :
    ReceiveTrackInPlayer {

    override fun execute(): Track {
        val json = searchHistoryRepository.sharedPreferences.getString(SELECTED_TRACK_KEY, null)
        return gsonJsonConverter.getTrackFromJson(json)
    }
}