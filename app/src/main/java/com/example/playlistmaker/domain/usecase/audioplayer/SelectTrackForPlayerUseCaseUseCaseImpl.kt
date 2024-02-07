package com.example.playlistmaker.domain.usecase.audioplayer

import com.example.playlistmaker.domain.Constants.SELECTED_TRACK_KEY
import com.example.playlistmaker.domain.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.GsonJsonConverter

class SelectTrackForPlayerUseCaseUseCaseImpl(private val searchHistoryRepository: SearchHistoryRepository, private val gsonJsonConverter: GsonJsonConverter):
    SelectTrackForPlayerUseCase {
    override fun execute(track: Track) {

        val json = gsonJsonConverter.getJsonFromTrack(track)

        searchHistoryRepository.sharedPreferences.edit().putString(SELECTED_TRACK_KEY, json).apply()
    }


}