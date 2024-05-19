package com.example.playlistmaker.data.createplaylist

import com.example.playlistmaker.data.converters.GeneralPlaylistDbConverter
import com.example.playlistmaker.data.converters.PlaylistDbConverter
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.playlists.PlaylistEntity
import com.example.playlistmaker.domain.createplaylist.PlaylistsRepository
import com.example.playlistmaker.domain.createplaylist.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val generalPlaylistDbConverter: GeneralPlaylistDbConverter
) : PlaylistsRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase
            .playlistsDao()
            .insertPlaylist(
                playlistDbConverter
                    .map(playlist)
            )
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase
            .playlistsDao()
            .updatePlaylist(
                playlistDbConverter
                    .map(playlist)
            )
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        appDatabase
            .playlistsDao()
            .deletePlaylist(
                playlistDbConverter
                    .map(playlist)
            )
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistsDao().getPlaylists()
        emit(convertFromPlaylistEntities(playlists))
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist {
        return playlistDbConverter
            .map(
                appDatabase
                    .playlistsDao()
                    .getPlaylistById(playlistId)
            )
    }

    override suspend fun getTracksIds(playlistId: Int): List<Int> {
        val playlist = getPlaylistById(playlistId)
        return playlist.trackIds
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist) {
        playlist.tracksQuantity += 1
        updatePlaylist(playlist)
    }

    override suspend fun deleteTrackFromPlaylist(playlist: Playlist) {
        playlist.tracksQuantity -= 1
        updatePlaylist(playlist)
    }

    override suspend fun addTrackToGeneralPlaylist(track: Track) {
        appDatabase
            .generalPlaylistDao()
            .insertTrackInGeneralPlaylist(
                generalPlaylistDbConverter.map(track)
            )
    }

    override suspend fun deleteTrackFromGeneralPlaylist(track: Track) {
        appDatabase
            .generalPlaylistDao()
            .deleteTrackFromGeneralPlaylist(
                generalPlaylistDbConverter.map(track)
            )
    }


    private fun convertFromPlaylistEntities(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConverter.map(playlist) }
    }


}