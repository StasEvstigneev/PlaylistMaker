package com.example.playlistmaker


import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.data.AudioPlayerImpl
import com.example.playlistmaker.data.GsonJsonConverterImpl
import com.example.playlistmaker.data.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.AudioPlayer
import com.example.playlistmaker.domain.SearchHistoryRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.usecase.audioplayer.ReceiveTrackInPlayerUseCase
import com.example.playlistmaker.domain.usecase.audioplayer.ReceiveTrackInPlayerUseCaseUseCaseImpl
import com.example.playlistmaker.domain.usecase.audioplayer.SelectTrackForPlayerUseCase
import com.example.playlistmaker.domain.usecase.audioplayer.SelectTrackForPlayerUseCaseUseCaseImpl
import com.example.playlistmaker.domain.usecase.searchhistory.AddElementToSearchHistoryUseCase
import com.example.playlistmaker.domain.usecase.searchhistory.AddElementToSearchHistoryUseCaseUseCaseImpl
import com.example.playlistmaker.domain.usecase.searchhistory.ClearSearchHistoryUseCase
import com.example.playlistmaker.domain.usecase.searchhistory.ClearSearchHistoryUseCaseUseCaseImpl
import com.example.playlistmaker.domain.usecase.searchhistory.GetSearchHistoryUseCase
import com.example.playlistmaker.domain.usecase.searchhistory.GetSearchHistoryUseCaseUseCaseImpl
import com.example.playlistmaker.domain.GsonJsonConverter


object Creator {

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun getAudioPlayer(): AudioPlayer {
        return AudioPlayerImpl(mediaPlayer = MediaPlayer())
    }

    fun provideSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(context)
    }

    fun provideGetSearchHistoryUseCase(searchHistoryRepository: SearchHistoryRepository, gsonJsonConverter: GsonJsonConverter): GetSearchHistoryUseCase {
        return GetSearchHistoryUseCaseUseCaseImpl(searchHistoryRepository, gsonJsonConverter)
    }

    fun provideAddElementToSearchHistoryUseCase(searchHistoryRepository: SearchHistoryRepository, getSearchHistoryUseCase: GetSearchHistoryUseCase, gsonJsonConverter: GsonJsonConverter): AddElementToSearchHistoryUseCase {
        return AddElementToSearchHistoryUseCaseUseCaseImpl(searchHistoryRepository,getSearchHistoryUseCase, gsonJsonConverter)
    }


    fun provideClearSearchHistoryUseCase(searchHistoryRepository: SearchHistoryRepository): ClearSearchHistoryUseCase {
        return ClearSearchHistoryUseCaseUseCaseImpl(searchHistoryRepository)

    }

    fun provideSelectTrackForPlayerUseCase(searchHistoryRepository: SearchHistoryRepository, gsonJsonConverter: GsonJsonConverter): SelectTrackForPlayerUseCase {
        return SelectTrackForPlayerUseCaseUseCaseImpl(searchHistoryRepository, gsonJsonConverter)
    }

    fun provideReceiveTrackInPlayerUseCase(searchHistoryRepository: SearchHistoryRepository, gsonJsonConverter: GsonJsonConverter): ReceiveTrackInPlayerUseCase {
        return ReceiveTrackInPlayerUseCaseUseCaseImpl(searchHistoryRepository, gsonJsonConverter)
    }

    fun provideGsonJsonConverter(): GsonJsonConverter {
        return GsonJsonConverterImpl
    }

}