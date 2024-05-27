package com.example.playlistmaker.presentation.editplaylist

import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.createplaylist.PlaylistsInteractor
import com.example.playlistmaker.presentation.createplaylist.CreatePlaylistViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditPlaylistViewModel(private val playlistsInteractor: PlaylistsInteractor) :
    CreatePlaylistViewModel(playlistsInteractor) {

    private var playlistId: Int? = null

    fun updatePlaylistId(id: Int) {
        playlistId = id

    }

    fun updatePlaylist() {
        viewModelScope.launch {

            withContext(Dispatchers.IO) {
                var playlist = playlistsInteractor.getPlaylistById(playlistId!!)

                playlist.title = title!!
                playlist.description = description
                playlist.coverPath = imagePath

                playlistsInteractor.updatePlaylist(playlist)
            }

        }

    }


}