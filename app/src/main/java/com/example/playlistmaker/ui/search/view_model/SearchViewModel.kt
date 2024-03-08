package com.example.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.mediateka.TracksInteractor
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.SearchTracksInteractor
import com.example.playlistmaker.domain.search.models.SearchState
import com.example.playlistmaker.domain.search.models.Track


class SearchViewModel(
    private val searchTracksInteractor: SearchTracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val tracksInteractor: TracksInteractor
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private var previousRequest: String = ""
    private var unprocessedRequest: String = ""

    private var searchResultsList = ArrayList<Track>()
    private var searchHistoryList = ArrayList<Track>()

    init {
        searchHistoryList = loadSearchHistory()

    }


    private val activityState =
        MutableLiveData<SearchState>(SearchState.Default(searchHistoryList, searchResultsList))

    fun getActivityState(): LiveData<SearchState> = activityState


    fun addTrackToSearchHistory(track: Track) {
        searchHistoryInteractor.addElementToSearchHistory(track)
        searchHistoryList = loadSearchHistory()
        activityState.postValue(SearchState.Default(searchHistoryList, searchResultsList))

    }

    fun clearSearchHistory() {
        searchHistoryInteractor.clearSearchHistory()
        searchHistoryList = loadSearchHistory()
        activityState.postValue(SearchState.Default(searchHistoryList, searchResultsList))
    }

    fun selectTrackForPlayer(selectedTrack: Track) {
        tracksInteractor.selectTrackForPlayer(selectedTrack)
    }

    private fun loadSearchHistory(): ArrayList<Track> {
        return searchHistoryInteractor.getSearchHistory()
    }


    fun clearSearchField() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        searchResultsList.clear()
        previousRequest = ""
        activityState.postValue(SearchState.Default(loadSearchHistory(), searchResultsList))
    }


    private fun search(searchRequest: String) {

        if (searchRequest.isNotBlank()) {

            searchResultsList.clear()
            unprocessedRequest = ""
            activityState.postValue(SearchState.Loading(searchResultsList))

            searchTracksInteractor.searchTracks(searchRequest, object : SearchTracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?, errorCode: Int?) {
                    handler.post {
                        if (errorCode == 523) {
                            activityState.postValue(SearchState.NoInternetConnectionError)
                            unprocessedRequest = searchRequest
                        } else if (foundTracks != null) {
                            searchResultsList.addAll(foundTracks)
                            activityState.postValue(SearchState.ShowSearchResults(foundTracks as ArrayList<Track>))
                        } else {
                            activityState.postValue(SearchState.NoResultsFoundError)
                        }

                    }
                }
            })

        }

        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

    }

    fun searchDebounce(searchRequest: String) {

        if (searchRequest != previousRequest) {
            previousRequest = searchRequest
            handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
            val searchRunnable = Runnable { search(searchRequest) }
            val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
            handler.postAtTime(
                searchRunnable,
                SEARCH_REQUEST_TOKEN,
                postTime
            )
        }
    }


    fun repeatRequest() {
        search(unprocessedRequest)
    }


    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }


}