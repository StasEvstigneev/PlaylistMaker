package com.example.playlistmaker.domain.player.usecases.impl

import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.GsonJsonConverter
import com.example.playlistmaker.domain.player.usecases.ReceiveTrackInPlayerUseCase
import com.example.playlistmaker.domain.storage.LocalStorage

class ReceiveTrackInPlayerUseCaseUseCaseImpl(private val localStorage: LocalStorage, private val gsonJsonConverter: GsonJsonConverter) :
    ReceiveTrackInPlayerUseCase {

    override fun execute(key: String): Track {
        val json = localStorage.getSavedStringData(key) ?: ""
        return gsonJsonConverter.getTrackFromJson(json)
    }
}