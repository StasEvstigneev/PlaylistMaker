package com.example.playlistmaker


import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.data.AudioPlayer
import com.example.playlistmaker.data.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.SearchHistoryRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.usecase.audioplayer.ReceiveTrackInPlayer
import com.example.playlistmaker.domain.usecase.audioplayer.ReceiveTrackInPlayerUseCase
import com.example.playlistmaker.domain.usecase.audioplayer.SelectTrackForPlayer
import com.example.playlistmaker.domain.usecase.audioplayer.SelectTrackForPlayerUseCase
import com.example.playlistmaker.domain.usecase.searchhistory.AddElementToSearchHistory
import com.example.playlistmaker.domain.usecase.searchhistory.AddElementToSearchHistoryUseCase
import com.example.playlistmaker.domain.usecase.searchhistory.ClearSearchHistory
import com.example.playlistmaker.domain.usecase.searchhistory.ClearSearchHistoryUseCase
import com.example.playlistmaker.domain.usecase.searchhistory.GetSearchHistory
import com.example.playlistmaker.domain.usecase.searchhistory.GetSearchHistoryUseCase
import com.example.playlistmaker.domain.GsonJsonConverter


object Creator {

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun getAudioPlayer(): AudioPlayer {
        return AudioPlayer(mediaPlayer = MediaPlayer())
    }

    fun provideSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(context)
    }

    fun provideGetSearchHistoryUseCase(searchHistoryRepository: SearchHistoryRepository, gsonJsonConverter: GsonJsonConverter): GetSearchHistory {
        return GetSearchHistoryUseCase(searchHistoryRepository, gsonJsonConverter)
    }

    fun provideAddElementToSearchHistoryUseCase(searchHistoryRepository: SearchHistoryRepository, getSearchHistoryUseCase: GetSearchHistory, gsonJsonConverter: GsonJsonConverter): AddElementToSearchHistory {
        return AddElementToSearchHistoryUseCase(searchHistoryRepository,getSearchHistoryUseCase, gsonJsonConverter)
    }


    fun provideClearSearchHistoryUseCase(searchHistoryRepository: SearchHistoryRepository): ClearSearchHistory {
        return ClearSearchHistoryUseCase(searchHistoryRepository)

    }

    fun provideSelectTrackForPlayerUseCase(searchHistoryRepository: SearchHistoryRepository, gsonJsonConverter: GsonJsonConverter): SelectTrackForPlayer {
        return SelectTrackForPlayerUseCase(searchHistoryRepository, gsonJsonConverter)
    }

    fun provideReceiveTrackInPlayerUseCase(searchHistoryRepository: SearchHistoryRepository, gsonJsonConverter: GsonJsonConverter): ReceiveTrackInPlayer {
        return ReceiveTrackInPlayerUseCase(searchHistoryRepository, gsonJsonConverter)
    }





}