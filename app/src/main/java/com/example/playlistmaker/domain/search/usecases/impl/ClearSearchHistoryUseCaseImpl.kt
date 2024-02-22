package com.example.playlistmaker.domain.search.usecases.impl

import com.example.playlistmaker.domain.search.usecases.ClearSearchHistoryUseCase
import com.example.playlistmaker.domain.storage.LocalStorage

class ClearSearchHistoryUseCaseImpl(private val localStorage: LocalStorage):
    ClearSearchHistoryUseCase {

    override fun execute() {
        localStorage.clearData()
    }
}