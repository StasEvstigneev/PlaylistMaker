package com.example.playlistmaker.data.sharing

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.model.EmailData

class ExternalNavigatorImpl(val context: Context) : ExternalNavigator {
    override fun shareLink() {
        val shareLink = getShareAppLink()

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareLink)
        }


        context.startActivity(
            Intent.createChooser(shareIntent, null)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    override fun openTerms() {
        val termsLink = getTermsLink()
        val userAgreementIntent = Intent(Intent.ACTION_VIEW, Uri.parse(termsLink))
        userAgreementIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(userAgreementIntent)

    }

    override fun sendEmail() {
        val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            val emailData = getSupportEmailData()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.emailAddress))
            putExtra(Intent.EXTRA_SUBJECT, emailData.emailSubject)
            putExtra(Intent.EXTRA_TEXT, emailData.emailMessage)
        }


        context.startActivity(
            Intent.createChooser(supportIntent, null)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }


    private fun getShareAppLink(): String {
        return context.getString(R.string.url_android_developer)
    }

    private fun getTermsLink(): String {
        return context.getString(R.string.url_user_agreement)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            emailAddress = context.getString(R.string.developer_email),
            emailSubject = context.getString(R.string.message_to_support_subject),
            emailMessage = context.getString(R.string.message_to_support_text)
        )
    }

}