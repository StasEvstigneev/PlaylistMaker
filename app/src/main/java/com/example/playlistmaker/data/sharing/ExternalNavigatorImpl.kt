package com.example.playlistmaker.data.sharing

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.model.EmailData

class ExternalNavigatorImpl(val context: Context): ExternalNavigator {
    override fun shareLink(shareLink: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            setType("text/plain")
            putExtra(Intent.EXTRA_TEXT, shareLink)
        }
        context.startActivity(Intent.createChooser(shareIntent, null))
    }

    override fun openTerms(termsLink: String) {
        val userAgreementIntent = Intent(Intent.ACTION_VIEW, Uri.parse(termsLink))
        userAgreementIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(userAgreementIntent)

    }

    override fun sendEmail(emailData: EmailData) {
        val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.emailAddress))
            putExtra(Intent.EXTRA_SUBJECT, emailData.emailSubject)
            putExtra(Intent.EXTRA_TEXT, emailData.emailMessage)
        }
        context.startActivity(Intent.createChooser(supportIntent, null))
    }

}