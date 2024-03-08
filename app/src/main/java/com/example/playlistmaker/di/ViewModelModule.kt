package com.example.playlistmaker.di

import com.example.playlistmaker.ui.mediateka.view_model.FavoriteTracksViewModel
import com.example.playlistmaker.ui.mediateka.view_model.PlaylistsViewModel
import com.example.playlistmaker.ui.player.view_model.AudioPlayerViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel<AudioPlayerViewModel> {
        AudioPlayerViewModel(get(), get())
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

}