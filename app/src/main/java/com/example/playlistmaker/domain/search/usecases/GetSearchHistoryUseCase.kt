package com.example.playlistmaker.domain.search.usecases

import com.example.playlistmaker.domain.search.models.Track

interface GetSearchHistoryUseCase {

    fun execute(key: String): ArrayList<Track>
}