package com.example.playlistmaker.ui.audioplayer.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.player.model.AudioPlayerScreenState
import com.example.playlistmaker.domain.player.model.AudioPlayerStatus
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.models.Track

class AudioPlayerViewModel(
    searchHistoryInteractor: SearchHistoryInteractor,
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
        val track = searchHistoryInteractor.receiveTackInPlayer()
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


    companion object {
        fun getViewModelFactory(
            searchHistoryInteractor: SearchHistoryInteractor,
            audioPlayerInteractor: AudioPlayerInteractor
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AudioPlayerViewModel(
                        searchHistoryInteractor,
                        audioPlayerInteractor
                    ) as T
                }
            }
    }
}