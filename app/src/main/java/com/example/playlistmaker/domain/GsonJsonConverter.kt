package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.search.models.Track

interface GsonJsonConverter {

    fun getTrackListFromJson(json: String): ArrayList<Track>

    fun getJsonFromArrayList(trackList: ArrayList<Track>): String

    fun getTrackFromJson(json: String): Track

    fun getJsonFromTrack(track: Track): String

    fun getTrackIdsFromJson(json: String): ArrayList<Int>

    fun getJsonFromTrackIds(trackIds: ArrayList<Int>): String


}