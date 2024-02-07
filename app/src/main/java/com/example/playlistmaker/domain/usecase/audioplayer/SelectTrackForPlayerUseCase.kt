package com.example.playlistmaker.domain.usecase.audioplayer

import com.example.playlistmaker.domain.models.Track

interface SelectTrackForPlayerUseCase {

    fun execute(track: Track)
}