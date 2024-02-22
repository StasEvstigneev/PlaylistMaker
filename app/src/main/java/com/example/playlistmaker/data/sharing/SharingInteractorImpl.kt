package com.example.playlistmaker.data.sharing

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val context:Context

): SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openTerms(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.sendEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return context.getString(R.string.url_android_developer)
    }

    private fun getTermsLink(): String {
        return  context.getString(R.string.url_user_agreement)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            emailAddress = context.getString(R.string.developer_email),
            emailSubject = context.getString(R.string.message_to_support_subject),
            emailMessage = context.getString(R.string.message_to_support_text)
        )
    }

}