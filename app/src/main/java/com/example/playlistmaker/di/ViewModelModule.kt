package com.example.playlistmaker.di

import com.example.playlistmaker.presentation.createplaylist.CreatePlaylistViewModel
import com.example.playlistmaker.presentation.mediateka.FavoriteTracksViewModel
import com.example.playlistmaker.presentation.mediateka.PlaylistsViewModel
import com.example.playlistmaker.presentation.player.AudioPlayerViewModel
import com.example.playlistmaker.presentation.search.SearchViewModel
import com.example.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel<AudioPlayerViewModel> {
        AudioPlayerViewModel(get(), get(), get())
    }

    viewModel<SearchViewModel> {
        SearchViewModel(get(), get(), get())
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(get(), get())
    }

    viewModel<FavoriteTracksViewModel> {
        FavoriteTracksViewModel(get())
    }

    viewModel<PlaylistsViewModel> {
        PlaylistsViewModel(get())
    }

    viewModel<CreatePlaylistViewModel> {
        CreatePlaylistViewModel(get())
    }

}