package com.example.playlistmaker.creator


import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmaker.data.player.AudioPlayerRepositoryImpl
import com.example.playlistmaker.utils.GsonJsonConverterImpl
import com.example.playlistmaker.data.search.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.settings.SettingsRepositoryImpl
import com.example.playlistmaker.data.sharing.ExternalNavigatorImpl
import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.GsonJsonConverter
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.data.sharing.SharingInteractorImpl
import com.example.playlistmaker.data.storage.LocalStorageImpl
import com.example.playlistmaker.domain.player.AudioPlayerRepository
import com.example.playlistmaker.domain.player.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.storage.LocalStorage


object Creator {

    private fun getTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }

    fun provideSearchHistoryInteractor(context:Context) : SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(context, provideGsonJsonConverter()))
    }

    fun getSearchHistoryRepository(context: Context, gsonJsonConverter: GsonJsonConverter): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(getLocalStorage(context.getSharedPreferences(SEARCH_HISTORY_PREFS, Context.MODE_PRIVATE)), gsonJsonConverter)
    }

    private fun getLocalStorage(sharedPreferences: SharedPreferences) : LocalStorage {
        return LocalStorageImpl(sharedPreferences)
    }


    fun provideSettingsInteractor(context:Context) : SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(getLocalStorage(context.getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)))
    }


    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(getAudioPlayerRepository())
    }

    private fun getAudioPlayerRepository(): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl(mediaPlayer = MediaPlayer())
    }


    fun provideGsonJsonConverter(): GsonJsonConverter {
        return GsonJsonConverterImpl
    }

    fun provideExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

    fun provideSharingInteractor(externalNavigator: ExternalNavigator, context: Context): SharingInteractor {
        return SharingInteractorImpl(externalNavigator, context)

    }

    private const val SEARCH_HISTORY_PREFS = "SharedPrefs"
    private const val SETTINGS_PREFERENCES = "settings_pref"


}