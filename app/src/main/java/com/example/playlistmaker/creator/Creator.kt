package com.example.playlistmaker.creator


import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.data.player.AudioPlayerImpl
import com.example.playlistmaker.utils.GsonJsonConverterImpl
import com.example.playlistmaker.data.search.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.search.TracksRepositoryImpl
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.settings.SettingsRepositoryImpl
import com.example.playlistmaker.data.sharing.ExternalNavigatorImpl
import com.example.playlistmaker.domain.player.AudioPlayer
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.player.usecases.ReceiveTrackInPlayerUseCase
import com.example.playlistmaker.domain.player.usecases.impl.ReceiveTrackInPlayerUseCaseUseCaseImpl
import com.example.playlistmaker.domain.player.usecases.SelectTrackForPlayerUseCase
import com.example.playlistmaker.domain.player.usecases.impl.SelectTrackForPlayerUseCaseUseCaseImpl
import com.example.playlistmaker.domain.search.usecases.AddElementToSearchHistoryUseCase
import com.example.playlistmaker.domain.search.usecases.impl.AddElementToSearchHistoryUseCaseImpl
import com.example.playlistmaker.domain.search.usecases.ClearSearchHistoryUseCase
import com.example.playlistmaker.domain.search.usecases.impl.ClearSearchHistoryUseCaseImpl
import com.example.playlistmaker.domain.search.usecases.GetSearchHistoryUseCase
import com.example.playlistmaker.domain.search.usecases.impl.GetSearchHistoryUseCaseImpl
import com.example.playlistmaker.domain.GsonJsonConverter
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.data.sharing.SharingInteractorImpl
import com.example.playlistmaker.data.storage.LocalStorageImpl
import com.example.playlistmaker.domain.storage.LocalStorage


object Creator {

    private fun getTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }

    fun getAudioPlayer(): AudioPlayer {
        return AudioPlayerImpl(mediaPlayer = MediaPlayer())
    }

    fun provideLocalStorage(context: Context, prefsName: String) : LocalStorage {
        return LocalStorageImpl(context, prefsName)
    }

    fun provideSearchHistoryRepository(context: Context, gsonJsonConverter: GsonJsonConverter): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(context, gsonJsonConverter)
    }

    fun provideGetSearchHistoryUseCase(localStorage: LocalStorage, gsonJsonConverter: GsonJsonConverter): GetSearchHistoryUseCase {
        return GetSearchHistoryUseCaseImpl(localStorage, gsonJsonConverter)
    }

    fun provideAddElementToSearchHistoryUseCase(localStorage: LocalStorage, getSearchHistoryUseCase: GetSearchHistoryUseCase, gsonJsonConverter: GsonJsonConverter): AddElementToSearchHistoryUseCase {
        return AddElementToSearchHistoryUseCaseImpl(localStorage, getSearchHistoryUseCase, gsonJsonConverter)
    }


    fun provideClearSearchHistoryUseCase(localStorage: LocalStorage): ClearSearchHistoryUseCase {
        return ClearSearchHistoryUseCaseImpl(localStorage)
    }

    fun provideSelectTrackForPlayerUseCase(localStorage: LocalStorage, gsonJsonConverter: GsonJsonConverter): SelectTrackForPlayerUseCase {
        return SelectTrackForPlayerUseCaseUseCaseImpl(localStorage, gsonJsonConverter)
    }

    fun provideReceiveTrackInPlayerUseCase(localStorage: LocalStorage, gsonJsonConverter: GsonJsonConverter): ReceiveTrackInPlayerUseCase {
        return ReceiveTrackInPlayerUseCaseUseCaseImpl(localStorage, gsonJsonConverter)
    }

    fun provideGsonJsonConverter(): GsonJsonConverter {
        return GsonJsonConverterImpl
    }

    fun provideSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }

    fun provideExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

    fun provideSharingInteractor(externalNavigator: ExternalNavigator, context: Context): SharingInteractor {
        return SharingInteractorImpl(externalNavigator, context)

    }


}