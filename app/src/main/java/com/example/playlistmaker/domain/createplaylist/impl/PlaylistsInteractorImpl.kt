package com.example.playlistmaker.domain.createplaylist.impl

import com.example.playlistmaker.domain.createplaylist.PlaylistsInteractor
import com.example.playlistmaker.domain.createplaylist.PlaylistsRepository
import com.example.playlistmaker.domain.createplaylist.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val repository: PlaylistsRepository): PlaylistsInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        repository.addPlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        repository.updatePlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        repository.deletePlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist {
        return repository.getPlaylistById(playlistId)
    }

    override suspend fun getTracksIds(playlistId: Int): List<Int> {
        return repository.getTracksIds(playlistId)
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist) {
        repository.addTrackToPlaylist(playlist)
    }

    override suspend fun deleteTrackFromPlaylist(playlist: Playlist) {
        repository.addTrackToPlaylist(playlist)
    }

    override suspend fun addTrackToGeneralPlaylist(track: Track) {
        repository.addTrackToGeneralPlaylist(track)
    }

    override suspend fun deleteTrackFromGeneralPlaylist(track: Track) {
        repository.deleteTrackFromGeneralPlaylist(track)
    }
}