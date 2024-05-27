package com.example.playlistmaker.data.sharing

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.createplaylist.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.model.EmailData
import com.example.playlistmaker.utils.Formatter

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

    override fun sharePlaylist(playlist: Playlist, tracks: ArrayList<Track>) {
        val playlistInfo = preparePlaylistInfo(playlist, tracks)

        val sharePlaylistIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, playlistInfo)
        }

        context.startActivity(
            Intent.createChooser(sharePlaylistIntent, null).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )

    }


    private fun preparePlaylistInfo(playlist: Playlist, tracks: ArrayList<Track>): String {

        val playlistTitle = playlist.title
        val playlistDescription =
            if (!playlist.description.isNullOrEmpty()) "\n${playlist.description}" else ""
        val tracksQuantity: String = Formatter
            .playlistTracksQuantityFormatter(playlist.tracksQuantity, context)

        var position: Int = 1
        var trackList: String = ""

        for (track in tracks) {
            trackList += "$position. ${track.artistName} - ${track.trackName} (${track.trackTime})\n"
            position += 1

        }

        return "$playlistTitle $playlistDescription\n$tracksQuantity\n$trackList"
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