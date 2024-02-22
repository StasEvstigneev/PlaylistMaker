package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer{
        fun consume(foundTracks: List<Track>?, errorCode: Int?)
    }
}