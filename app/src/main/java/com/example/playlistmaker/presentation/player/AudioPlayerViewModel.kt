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
    tracksInteractor: TracksInteractor,
    private val audioPlayerInteractor: AudioPlayerInteractor
) : ViewModel() {

    private var playbackTimerJob: Job? = null


    private var screenStateLiveData =
        MutableLiveData<AudioPlayerScreenState>(AudioPlayerScreenState.Loading)

    fun getScreenStateLiveData(): LiveData<AudioPlayerScreenState> = screenStateLiveData


    private var playerStateLiveData = MutableLiveData<AudioPlayerState>(AudioPlayerState
        .Default())
    fun getPlayerStateLiveData(): LiveData<AudioPlayerState> = playerStateLiveData



    private var playbackTimer = MutableLiveData<String>(resetPlaybackTimer())
    fun getPlaybackTimerLiveData(): LiveData<String> = playbackTimer


    init {
        val track = tracksInteractor.uploadTrackInPlayer()
        screenStateLiveData.setValue(AudioPlayerScreenState
            .TrackIsLoaded(track))
        preparePlayer(track)

    }

    private fun preparePlayer(track: Track) {
        audioPlayerInteractor.preparePlayer(track)
        audioPlayerInteractor.setOnPreparedListener {
            playerStateLiveData.postValue(AudioPlayerState.Prepared())
        }
        audioPlayerInteractor.setOnCompletionListener {
            playerStateLiveData.postValue(AudioPlayerState.Prepared())
            playbackTimerJob?.cancel()
            playbackTimer.postValue(resetPlaybackTimer())
        }
    }


    private fun startPlayer() {
        audioPlayerInteractor.startPlayer()
        playerStateLiveData.postValue(AudioPlayerState.Playing(isPlaying()))
        runPlaybackTimer()
    }

    fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
        playbackTimerJob?.cancel()
        playerStateLiveData.postValue(AudioPlayerState.Paused(isPlaying()))
    }

    fun playbackControl() {
        when (playerStateLiveData.value) {
            is AudioPlayerState.Playing -> pausePlayer()
            is AudioPlayerState.Paused -> startPlayer()
            is AudioPlayerState.Prepared -> startPlayer()
            else -> false
        }
    }


    fun onPauseControl() {
        when (playerStateLiveData.value) {
            is AudioPlayerState.Playing -> playbackControl()
            else -> {}
        }
    }

    fun onResumeControl() {
        when (playerStateLiveData.value) {
            is AudioPlayerState.Paused -> playbackControl()
            else -> {}
        }
    }

    private fun isPlaying(): Boolean {
        return audioPlayerInteractor.isPlaying()
    }

    private fun releasePlayer() {
        audioPlayerInteractor.releasePlayer()
        playerStateLiveData.postValue(AudioPlayerState.Default())
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

    companion object{
        private const val PLAYER_POSITION_CHECK_INTERVAL_MILLIS = 300L
    }

}