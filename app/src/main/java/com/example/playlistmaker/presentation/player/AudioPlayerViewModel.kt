package com.example.playlistmaker.presentation.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.mediateka.TracksInteractor
import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.player.model.AudioPlayerScreenState
import com.example.playlistmaker.domain.player.model.AudioPlayerState
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val tracksInteractor: TracksInteractor,
    private val audioPlayerInteractor: AudioPlayerInteractor
) : ViewModel() {

    private var playbackTimerJob: Job? = null

    private var track: Track = tracksInteractor.uploadTrackInPlayer()

    private var screenState =
        MutableLiveData<AudioPlayerScreenState>(AudioPlayerScreenState.Loading)

    fun getScreenState(): LiveData<AudioPlayerScreenState> = screenState


    private var playerState = MutableLiveData<AudioPlayerState>(
        AudioPlayerState
            .Default()
    )

    fun getPlayerState(): LiveData<AudioPlayerState> = playerState


    private var playbackTimer = MutableLiveData<String>(resetPlaybackTimer())
    fun getPlaybackTimerLiveData(): LiveData<String> = playbackTimer


    private var favoriteStatus = MutableLiveData<Boolean>()
    fun getFavoriteStatus(): LiveData<Boolean> = favoriteStatus

    init {
        preparePlayer(track)
        screenState.postValue(
            AudioPlayerScreenState
                .TrackIsLoaded(track)
        )

        viewModelScope.launch {
            tracksInteractor.getFavoriteTracksIds()
                .collect { ids ->
                    if (ids.contains(track.trackId)) {
                        favoriteStatus.postValue(true)
                    } else {
                        favoriteStatus.postValue(false)
                    }
                }
        }

    }


    fun onFavoriteClicked() {

        if (favoriteStatus.value == true) {
            removeFromFavorites(track)
            favoriteStatus.postValue(false)

        } else {
            addToFavorites(track)
            favoriteStatus.postValue(true)
        }
    }


    private fun addToFavorites(track: Track) {
        viewModelScope.launch { tracksInteractor.addTrackToFavorites(track) }
    }

    private fun removeFromFavorites(track: Track) {
        viewModelScope.launch { tracksInteractor.removeTrackFromFavorites(track) }
    }

    private fun preparePlayer(track: Track) {
        audioPlayerInteractor.preparePlayer(track)
        audioPlayerInteractor.setOnPreparedListener {
            playerState.postValue(AudioPlayerState.Prepared())
        }
        audioPlayerInteractor.setOnCompletionListener {
            playerState.postValue(AudioPlayerState.Prepared())
            playbackTimerJob?.cancel()
            playbackTimer.postValue(resetPlaybackTimer())
        }
    }


    private fun startPlayer() {
        audioPlayerInteractor.startPlayer()
        playerState.postValue(AudioPlayerState.Playing(isPlaying()))
        runPlaybackTimer()
    }

    fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
        playbackTimerJob?.cancel()
        playerState.postValue(AudioPlayerState.Paused(isPlaying()))
    }

    fun playbackControl() {
        when (playerState.value) {
            is AudioPlayerState.Playing -> pausePlayer()
            is AudioPlayerState.Paused -> startPlayer()
            is AudioPlayerState.Prepared -> startPlayer()
            else -> false
        }
    }


    fun onPauseControl() {
        when (playerState.value) {
            is AudioPlayerState.Playing -> playbackControl()
            else -> {}
        }
    }

    fun onResumeControl() {
        when (playerState.value) {
            is AudioPlayerState.Paused -> playbackControl()
            else -> {}
        }
    }

    private fun isPlaying(): Boolean {
        return audioPlayerInteractor.isPlaying()
    }

    private fun releasePlayer() {
        audioPlayerInteractor.releasePlayer()
        playerState.postValue(AudioPlayerState.Default())
    }

    private fun runPlaybackTimer() {
        playbackTimerJob = viewModelScope.launch {
            while (isPlaying()) {
                delay(PLAYER_POSITION_CHECK_INTERVAL_MILLIS)
                playbackTimer.postValue(getPlayingProgress())
            }
        }
    }

    private fun resetPlaybackTimer(): String {
        return audioPlayerInteractor.resetTrackPlaybackTime()
    }

    private fun getPlayingProgress(): String {
        return audioPlayerInteractor.getCurrentPosition()
    }


    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    companion object {
        private const val PLAYER_POSITION_CHECK_INTERVAL_MILLIS = 300L
    }

}