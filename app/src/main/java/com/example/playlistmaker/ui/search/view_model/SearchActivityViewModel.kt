package com.example.playlistmaker.ui.search.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.models.SearchActivityState
import com.example.playlistmaker.domain.search.models.Track


class SearchActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val tracksInteractor = Creator.provideTracksInteractor(application)
    private val handler = Handler(Looper.getMainLooper())

    private val searchHistoryRepository = Creator
        .provideSearchHistoryRepository(
            application.applicationContext,
            Creator.provideGsonJsonConverter()
        )

    private var previousRequest: String = ""

    fun setPreviousRequest(newSearchRequest: String) {
        previousRequest = newSearchRequest
    }

    fun getPreviousRequest(): String {
        return previousRequest
    }

    private var searchResultsList = ArrayList<Track>()
    private var searchHistoryList = loadSearchHistory()

    private val activityState = MutableLiveData<SearchActivityState>(SearchActivityState.Default())
    fun getActivityState(): LiveData<SearchActivityState> = activityState


    private val searchHistoryLiveData = MutableLiveData(searchHistoryList)
    fun getSearchHistoryLiveData(): LiveData<ArrayList<Track>> = searchHistoryLiveData


    private val searchResultsLiveData = MutableLiveData(searchResultsList)
    fun getSearchResultsLiveData(): LiveData<ArrayList<Track>> = searchResultsLiveData

    fun addTrackToSearchHistory(track: Track) {
        searchHistoryRepository.addElementToSearchHistory(track)
        searchHistoryLiveData.postValue(loadSearchHistory())
    }

    fun clearSearchHistory() {
        searchHistoryRepository.clearSearchHistory()
        searchHistoryLiveData.postValue(loadSearchHistory())
        activityState.postValue(SearchActivityState.Default())
    }


    fun selectTrackForPlayer(selectedTrack: Track) {
        searchHistoryRepository.selectTrackForPlayer(selectedTrack)
    }

    fun loadSearchHistory(): ArrayList<Track> {
        return searchHistoryRepository.getSearchHistory()
    }

    fun receiveSearchResults(): ArrayList<Track> {
        return searchResultsList
    }

    fun clearSearchField() {
        searchResultsList.clear()
        previousRequest = ""
        activityState.postValue(SearchActivityState.Default())
    }


    private fun search(searchRequest: String) {

        if (searchRequest.isNotBlank()) {
            searchResultsList.clear()
            searchResultsLiveData.postValue(searchResultsList)
            activityState.postValue(SearchActivityState.Loading())

            tracksInteractor.searchTracks(searchRequest, object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?, errorCode: Int?) {
                    handler.post {
                        if (errorCode == 523) {
                            activityState.postValue(SearchActivityState.NoInternetConnectionError())
                        } else if (foundTracks != null) {
                            searchResultsList.addAll(foundTracks)
                            searchResultsLiveData.postValue(searchResultsList)
                            activityState.postValue(SearchActivityState.ShowResults())
                        } else {
                            activityState.postValue(SearchActivityState.NoResultsFoundError())
                        }

                    }
                }
            })

        }
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(searchRequest: String) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchRunnable = Runnable { search(searchRequest) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime
        )

    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchActivityViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }
}