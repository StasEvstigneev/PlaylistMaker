package com.example.playlistmaker.data.search

import com.example.playlistmaker.utils.Formatter
import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.data.search.dto.TrackSearchResultsResponse
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.models.Resource
import com.example.playlistmaker.domain.search.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient): TracksRepository {
    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error(523)
            }
            200 -> {
                Resource.Success((response as TrackSearchResultsResponse).results.map {
                Track(it.trackId ?: EMPTY_INT,
                    it.trackName ?: EMPTY_STRING,
                    it.artistName ?: EMPTY_STRING,
                    Formatter.getTimeMMSS(it.trackTime ?: EMPTY_LONG),
                    it.artworkUrl100 ?: EMPTY_STRING,
                    Formatter.getArtworkUrl512(it.artworkUrl100 ?: EMPTY_STRING),
                    it.collectionName ?: EMPTY_STRING,
                    it.releaseDate ?: EMPTY_STRING ,
                    it.primaryGenreName ?: EMPTY_STRING,
                    it.country ?: EMPTY_STRING,
                    it.previewUrl ?: EMPTY_STRING)
            })

            } else -> {
                Resource.Error(404)
            }
        }

    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val EMPTY_LONG = 0L
        private const val EMPTY_INT = 0
    }


}