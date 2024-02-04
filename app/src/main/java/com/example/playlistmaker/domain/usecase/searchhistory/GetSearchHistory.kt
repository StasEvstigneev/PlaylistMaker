package com.example.playlistmaker.domain.usecase.searchhistory

import com.example.playlistmaker.domain.models.Track

interface GetSearchHistory {

    fun execute(): ArrayList<Track>
}