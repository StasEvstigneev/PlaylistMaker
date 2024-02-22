package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.search.models.Resource

interface TracksRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}