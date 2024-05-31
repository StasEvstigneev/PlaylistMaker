package com.example.playlistmaker.domain.sharing

import com.example.playlistmaker.domain.createplaylist.models.Playlist
import com.example.playlistmaker.domain.search.models.Track

interface ExternalNavigator {

    fun shareLink()

    fun openTerms()

    fun sendEmail()
    fun sharePlaylist(playlist: Playlist, tracks: ArrayList<Track>)
}