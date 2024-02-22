package com.example.playlistmaker.domain.player.usecases

import com.example.playlistmaker.domain.search.models.Track

interface ReceiveTrackInPlayerUseCase {

    fun execute(key: String): Track
}