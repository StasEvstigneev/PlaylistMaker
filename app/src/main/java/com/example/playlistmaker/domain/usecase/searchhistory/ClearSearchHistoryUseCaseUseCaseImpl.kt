package com.example.playlistmaker.domain.usecase.searchhistory

import com.example.playlistmaker.domain.SearchHistoryRepository

class ClearSearchHistoryUseCaseUseCaseImpl(private val searchHistoryRepository: SearchHistoryRepository):
    ClearSearchHistoryUseCase {

    override fun execute() {
        searchHistoryRepository.sharedPreferences.edit().clear().apply()
    }
}