package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.search.dto.TrackSearchResultsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesApiService {
    @GET("search?entity=song")
    suspend fun search(
        @Query("term") text: String
    ): TrackSearchResultsResponse
}