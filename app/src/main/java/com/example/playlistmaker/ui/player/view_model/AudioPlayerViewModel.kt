package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.mediateka.TracksInteractor
import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.player.model.AudioPlayerScreenState
import com.example.playlistmaker.domain.player.model.AudioPlayerStatus
import com.example.playlistmaker.domain.search.models.Track

class AudioPlayerViewModel(
    tracksInteractor: TracksInteractor,
    private val audioPlayerInteractor: AudioPlayerInteractor
) : ViewModel() {

    private val playbackTimerUpdater = Runnable { updatePlaybackTimer() }

    private var screenStateLiveData =
        MutableLiveData<AudioPlayerScreenState>(AudioPlayerScreenState.Loading)

    fun getScreenStateLiveData(): LiveData<AudioPlayerScreenState> = screenStateLiveData

    private var trackPlaybackTimerLiveData =
        MutableLiveData(audioPlayerInteractor.resetTrackPlaybackTime())

    fun getTrackPlaybackTimerLiveData(): LiveData<String> = trackPlaybackTimerLiveData

    private var playerStatusLiveData = MutableLiveData<AudioPlayerStatus>(AudioPlayerStatus.DEFAULT)
    fun getPlayerStatusLiveData(): LiveData<AudioPlayerStatus> = playerStatusLiveData


    init {
        val track = tracksInteractor.receiveTackInPlayer()
        screenStateLiveData.setValue(AudioPlayerScreenState.Content(track))
        preparePlayer(track)

    }

    private fun preparePlayer(track: Track) {
        audioPlayerInteractor.preparePlayer(track)
        audioPlayerInteractor.setOnPreparedListener {
            playerStatusLiveData.postValue(AudioPlayerStatus.PREPARED)
            audioPlayerInteractor.setPlayerStatePrepared()
        }
        audioPlayerInteractor.setOnCompletionListener {
            playerStatusLiveData.postValue(AudioPlayerStatus.PREPARED)
            audioPlayerInteractor.setPlayerStatePrepared()
        }
    }


    private fun startPlayer() {
        audioPlayerInteractor.startPlayer()
        playerStatusLiveData.postValue(AudioPlayerStatus.PLAYING)
        playbackTimerUpdater.run()

    }

    fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
        playerStatusLiveData.postValue(AudioPlayerStatus.PAUSED)
    }

    fun playbackControl() {
        if (audioPlayerInteractor.isPlaying()) {
            pausePlayer()
        } else {
            startPlayer()
        }
    }

    private fun releasePlayer() {
        audioPlayerInteractor.releasePlayer(playbackTimerUpdater)
    }

    private fun updatePlaybackTimer() {
        trackPlaybackTimerLiveData.postValue(
            audioPlayerInteractor.updatePlaybackTimer(
                playbackTimerUpdater
            )
        )

    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

}