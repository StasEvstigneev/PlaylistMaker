package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.SharingInteractor


class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator

): SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink()
    }

    override fun openTerms() {
        externalNavigator.openTerms()
    }

    override fun openSupport() {
        externalNavigator.sendEmail()
    }

}