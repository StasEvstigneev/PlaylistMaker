package com.example.playlistmaker.domain.player.usecases

import com.example.playlistmaker.domain.search.models.Track

interface SelectTrackForPlayerUseCase {

    fun execute(key: String, track: Track)
}