package com.example.playlistmaker.domain.search.usecases.impl

import com.example.playlistmaker.domain.GsonJsonConverter
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.search.usecases.GetSearchHistoryUseCase
import com.example.playlistmaker.domain.storage.LocalStorage


class GetSearchHistoryUseCaseImpl(private val localStorage: LocalStorage, private val gsonJsonConverter: GsonJsonConverter):
    GetSearchHistoryUseCase {

    override fun execute(key: String): ArrayList<Track> {
        val json = localStorage.getSavedStringData(key)
        return gsonJsonConverter.getTrackListFromJson(json)
    }
}