package com.example.playlistmaker.utils

import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.GsonJsonConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object GsonJsonConverterImpl: GsonJsonConverter {
    override fun getTrackListFromJson(json: String): ArrayList<Track> {
        return Gson().fromJson(json, object : TypeToken<ArrayList<Track>>() {}.type) ?: ArrayList()
    }

    override fun getJsonFromArrayList(trackList: ArrayList<Track>): String {
        return Gson().toJson(trackList)
    }

    override fun getTrackFromJson(json: String): Track {
        return Gson().fromJson(json, object : TypeToken<Track>() {}.type)
    }

    override fun getJsonFromTrack(track: Track): String {
        return Gson().toJson(track)
    }


}