package com.example.playlistmaker.domain.player.usecases.impl

import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.GsonJsonConverter
import com.example.playlistmaker.domain.player.usecases.SelectTrackForPlayerUseCase
import com.example.playlistmaker.domain.storage.LocalStorage

class SelectTrackForPlayerUseCaseUseCaseImpl(private val localStorage: LocalStorage, private val gsonJsonConverter: GsonJsonConverter):
    SelectTrackForPlayerUseCase {
    override fun execute(key: String, track: Track) {
        val json:String = gsonJsonConverter.getJsonFromTrack(track)
        localStorage.addStringData(key, json)
    }


}