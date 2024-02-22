package com.example.playlistmaker.ui.audioplayer.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.player.model.AudioPlayerScreenState
import com.example.playlistmaker.domain.player.model.AudioPlayerStatus
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.models.Track

class AudioPlayerViewModel(
    searchHistoryRepository: SearchHistoryRepository
): ViewModel() {

    private val audioPlayer = Creator.getAudioPlayer()
    private val playbackTimerUpdater = Runnable { updatePlaybackTimer() }

    private var screenStateLiveData = MutableLiveData<AudioPlayerScreenState>(AudioPlayerScreenState.Loading)
    fun getScreenStateLiveData(): LiveData<AudioPlayerScreenState> = screenStateLiveData

    private var trackPlaybackTimerLiveData = MutableLiveData(audioPlayer.resetTrackPlaybackTime())
    fun getTrackPlaybackTimerLiveData(): LiveData<String> = trackPlaybackTimerLiveData

    private var playerStatusLiveData = MutableLiveData<AudioPlayerStatus>(AudioPlayerStatus.DEFAULT)
    fun getPlayerStatusLiveData(): LiveData<AudioPlayerStatus> = playerStatusLiveData


    init {
        val track = searchHistoryRepository.receiveTackInPlayer()
        screenStateLiveData.setValue(AudioPlayerScreenState.Content(track))
        preparePlayer(track)

    }

    private fun preparePlayer(track: Track) {
        audioPlayer.preparePlayer(track)
        audioPlayer.setOnPreparedListener {
            playerStatusLiveData.postValue(AudioPlayerStatus.PREPARED)
            audioPlayer.setPlayerStatePrepared()
        }
        audioPlayer.setOnCompletionListener {
            playerStatusLiveData.postValue(AudioPlayerStatus.PREPARED)
            audioPlayer.setPlayerStatePrepared()
        }
    }


    private fun startPlayer() {
        audioPlayer.startPlayer()
        playerStatusLiveData.postValue(AudioPlayerStatus.PLAYING)
        playbackTimerUpdater.run()

    }

    fun pausePlayer() {
        audioPlayer.pausePlayer()
        playerStatusLiveData.postValue(AudioPlayerStatus.PAUSED)
    }

    fun playbackControl() {
        if (audioPlayer.isPlaying()) {
            pausePlayer()
        } else {
            startPlayer()
        }
    }

    fun releasePlayer() {
        audioPlayer.releasePlayer(playbackTimerUpdater)
    }

    private fun updatePlaybackTimer() {
    trackPlaybackTimerLiveData.postValue(audioPlayer.updatePlaybackTimer(playbackTimerUpdater))

    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }


    companion object {
        fun getViewModelFactory(searchHistoryRepository: SearchHistoryRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AudioPlayerViewModel(
                        searchHistoryRepository
                    ) as T
                }
            }
    }
}