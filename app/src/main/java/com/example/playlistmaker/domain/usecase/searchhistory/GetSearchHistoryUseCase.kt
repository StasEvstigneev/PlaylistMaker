package com.example.playlistmaker.domain.usecase.searchhistory

import com.example.playlistmaker.domain.models.Track

interface GetSearchHistoryUseCase {

    fun execute(): ArrayList<Track>
}