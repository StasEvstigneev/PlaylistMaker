package com.example.playlistmaker.di

import com.example.playlistmaker.data.mediateka.TracksRepositoryImpl
import com.example.playlistmaker.data.player.AudioPlayerRepositoryImpl
import com.example.playlistmaker.data.search.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.search.impl.SearchTracksRepositoryImpl
import com.example.playlistmaker.data.settings.SettingsRepositoryImpl
import com.example.playlistmaker.domain.mediateka.TracksRepository
import com.example.playlistmaker.domain.player.AudioPlayerRepository
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.SearchTracksRepository
import com.example.playlistmaker.domain.settings.SettingsRepository
import org.koin.dsl.module


val repositoryModule = module {

    factory<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get())
    }

    single<SearchTracksRepository> {
        SearchTracksRepositoryImpl(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get(), get())
    }

}