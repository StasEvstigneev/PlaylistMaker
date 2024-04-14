package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.search.models.Resource
import kotlinx.coroutines.flow.Flow

interface SearchTracksRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}