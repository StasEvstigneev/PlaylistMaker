package com.example.playlistmaker.utils

import android.content.Context
import com.example.playlistmaker.R
import java.text.SimpleDateFormat
import java.util.Locale

object Formatter {

    fun getTimeMMSS(time: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(time)
    }


    fun getTimeMillis(time: String): Long {
        val seconds = time.takeLast(2).toLong()
        val minutes = time.dropLast(3).toLong()
        val timeInMillis = (minutes * 60 + seconds) * 1000

        return timeInMillis

    }


    fun getArtworkUrl512(artworkUrl: String): String {
        return artworkUrl
            .replaceAfterLast('/', "512x512bb.jpg")
    }


    fun getYearFromReleaseDate(releaseDate: String): String {
        return if (releaseDate.length > 4) {
            releaseDate.substring(0, 4)
        } else {
            releaseDate
        }
    }


    fun playlistTracksQuantityFormatter(quantity: Int, context: Context): String {
        val result: String = quantity.toString()
        val resultLastTwo = result.takeLast(2).toInt()
        val resultLastOne = result.takeLast(1).toInt()

        var trackWordForm: String

        when (resultLastTwo) {
            in 11..19 -> {
                trackWordForm = context.getString(R.string.tracks_genitive)

            }

            else -> {
                if (resultLastOne == 0 || resultLastOne in 5..9) {
                    trackWordForm = context.getString(R.string.tracks_genitive)
                } else if (resultLastOne in 2..4) {
                    trackWordForm = context.getString(R.string.track_genitive)
                } else {
                    trackWordForm = context.getString(R.string.track)
                }
            }
        }

        return result + " " + trackWordForm
    }


    fun playlistTotalDurationFormatter(duration: Long, context: Context): String {
        val result = (duration / 60000).toString()
        val resultLastTwo = result.takeLast(2).toInt()
        val resultLastOne = result.takeLast(1).toInt()

        var trackWordForm: String

        when (resultLastTwo) {
            in 11..19 -> {
                trackWordForm = context.getString(R.string.minutes_genitive)

            }

            else -> {
                if (resultLastOne == 0 || resultLastOne in 5..9) {
                    trackWordForm = context.getString(R.string.minutes_genitive)
                } else if (resultLastOne in 2..4) {
                    trackWordForm = context.getString(R.string.minute_genitive)
                } else {
                    trackWordForm = context.getString(R.string.minute)
                }
            }
        }

        return result + " " + trackWordForm
    }


}