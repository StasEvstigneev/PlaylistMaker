package com.example.playlistmaker.data.createplaylist

import android.util.Log
import com.example.playlistmaker.data.converters.GeneralPlaylistDbConverter
import com.example.playlistmaker.data.converters.PlaylistDbConverter
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.general_playlist.GeneralPlaylistEntity
import com.example.playlistmaker.data.db.playlists.PlaylistEntity
import com.example.playlistmaker.domain.createplaylist.PlaylistsRepository
import com.example.playlistmaker.domain.createplaylist.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.utils.Formatter
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


    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        playlist.tracksIds.add(track.trackId)
        playlist.tracksQuantity += 1
        updatePlaylist(playlist)
        addTrackToGeneralPlaylist(track)
    }

    override suspend fun deleteTrackFromPlaylist(playlist: Playlist, track: Track) {

        if (playlist.tracksIds.contains(track.trackId)) {
            playlist.tracksIds.remove(track.trackId)
            playlist.tracksQuantity -= 1
        }
        updatePlaylist(playlist)

        getPlaylists().collect { playlists ->
            val allTracks: ArrayList<Int> = arrayListOf()
            for (playlist in playlists) {
                allTracks.addAll(playlist.tracksIds)
            }
            if (!allTracks.contains(track.trackId)) {
                deleteTrackFromGeneralPlaylist(track)
            }
        }

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


    override suspend fun getTracksIds(playlistId: Int): Flow<List<Int>> = flow {
        val tracksIds = appDatabase
            .playlistsDao()
            .getTracksIds(playlistId)
        emit(playlistDbConverter.mapTracksIds(tracksIds))

    }

    override suspend fun getTracksByIds(ids: List<Int>): Flow<List<Track>> = flow {
        val tracks = appDatabase.generalPlaylistDao().getTracksByIds(ids)
        emit(convertFromGeneralPlaylistEntities(tracks))
    }

    override fun getTracklistDuration(tracks: ArrayList<Track>?): Long {
        var totalDurationMillis: Long = 0L

        if (!tracks.isNullOrEmpty()) {
            for (track in tracks) {
                val trackTime = Formatter.getTimeMillis(track.trackTime)
                Log.d("Track time", "$trackTime")
                totalDurationMillis += trackTime

            }
        }

        return totalDurationMillis
    }


    private fun convertFromPlaylistEntities(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConverter.map(playlist) }
    }


    private fun convertFromGeneralPlaylistEntities(tracks: List<GeneralPlaylistEntity>): List<Track> {
        return tracks.map { track -> generalPlaylistDbConverter.map(track) }
    }


}