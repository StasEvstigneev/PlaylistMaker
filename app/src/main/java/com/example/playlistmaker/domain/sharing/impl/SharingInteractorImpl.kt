package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.domain.createplaylist.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.SharingInteractor


class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator

) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink()
    }

    override fun openTerms() {
        externalNavigator.openTerms()
    }

    override fun openSupport() {
        externalNavigator.sendEmail()
    }

    override fun sharePlaylist(playlist: Playlist, tracks: ArrayList<Track>) {
        externalNavigator.sharePlaylist(playlist, tracks)
    }

}