package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.utils.Formatter
import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.data.search.dto.TrackSearchResultsResponse
import com.example.playlistmaker.domain.search.SearchTracksRepository
import com.example.playlistmaker.domain.search.models.Resource
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchTracksRepositoryImpl(private val networkClient: NetworkClient): SearchTracksRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
       when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(INTERNET_CONNECTION_ERROR))
            }
            200 -> {
                emit(
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
                            it.previewUrl ?: EMPTY_STRING
                        )
                    })
                )

            } else -> {
                emit(Resource.Error(NOTHING_FOUND_ERROR))
            }
        }

    }


    companion object {
        private const val EMPTY_STRING = ""
        private const val EMPTY_LONG = 0L
        private const val EMPTY_INT = 0
        private const val INTERNET_CONNECTION_ERROR = 523
        private const val NOTHING_FOUND_ERROR = 404
    }


}