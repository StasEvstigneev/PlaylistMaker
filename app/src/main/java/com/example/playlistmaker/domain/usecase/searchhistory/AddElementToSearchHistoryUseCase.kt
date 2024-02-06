package com.example.playlistmaker.domain.usecase.searchhistory

import com.example.playlistmaker.domain.models.Track

interface AddElementToSearchHistoryUseCase {

    fun execute(newTrack: Track)

}