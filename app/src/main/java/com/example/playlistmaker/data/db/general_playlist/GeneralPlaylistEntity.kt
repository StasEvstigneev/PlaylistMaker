package com.example.playlistmaker.data.db.general_playlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "general_playlist")
data class GeneralPlaylistEntity(
    @PrimaryKey @ColumnInfo(name = "track_id")
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String,
    val artworkUrl512: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    @ColumnInfo(name = "insertion_time")
    val timeStamp: Long = System.currentTimeMillis()


)
