package com.example.playlistmaker.domain.createplaylist

import com.example.playlistmaker.domain.createplaylist.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun getPlaylistById(playlistId: Int): Playlist

    suspend fun getTracksIds(playlistId: Int): List<Int>

    suspend fun addTrackToPlaylist(playlist: Playlist)

    suspend fun deleteTrackFromPlaylist(playlist: Playlist)

    suspend fun addTrackToGeneralPlaylist(track: Track)

    suspend fun deleteTrackFromGeneralPlaylist(track: Track)


}