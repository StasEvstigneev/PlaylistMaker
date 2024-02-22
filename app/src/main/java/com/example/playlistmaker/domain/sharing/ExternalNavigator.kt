package com.example.playlistmaker.domain.sharing

import com.example.playlistmaker.domain.sharing.model.EmailData

interface ExternalNavigator {

    fun shareLink(shareLink: String)

    fun openTerms(termsLink: String)

    fun sendEmail(emailData: EmailData)
}