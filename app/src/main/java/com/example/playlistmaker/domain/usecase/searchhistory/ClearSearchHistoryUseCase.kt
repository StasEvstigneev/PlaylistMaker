package com.example.playlistmaker.domain.usecase.searchhistory

import com.example.playlistmaker.domain.SearchHistoryRepository

class ClearSearchHistoryUseCase(private val searchHistoryRepository: SearchHistoryRepository):
    ClearSearchHistory {

    override fun execute() {
        searchHistoryRepository.sharedPreferences.edit().clear().apply()
    }
}