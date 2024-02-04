package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient: NetworkClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesApiService = retrofit.create(iTunesApiService::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            val resp = iTunesApiService.search(dto.expression).execute()

            val body = resp.body() ?: Response()

            return body.apply {resultCode = resp.code()}
        } else {
            return Response().apply{resultCode = 400}
        }

    }

    companion object {

        private const val ITUNES_BASE_URL = "https://itunes.apple.com"
//        private const val SEARCH_DEBOUNCE_DELAY = 2000L
//        private const val SEARCH_RESULTS_CLICK_DELAY = 1000L
    }
}