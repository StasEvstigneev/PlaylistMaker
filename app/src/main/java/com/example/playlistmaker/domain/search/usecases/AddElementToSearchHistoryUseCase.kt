package com.example.playlistmaker.domain.search.usecases

import com.example.playlistmaker.domain.search.models.Track

interface AddElementToSearchHistoryUseCase {

    fun execute(key:String, newTrack: Track, itemsLimit: Int)

}