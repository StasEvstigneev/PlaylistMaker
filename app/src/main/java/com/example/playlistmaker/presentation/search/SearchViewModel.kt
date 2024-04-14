package com.example.playlistmaker.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.mediateka.TracksInteractor
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.SearchTracksInteractor
import com.example.playlistmaker.domain.search.models.SearchState
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchViewModel(
    private val searchTracksInteractor: SearchTracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val tracksInteractor: TracksInteractor
) : ViewModel() {

    private var previousRequest: String = ""
    private var unprocessedRequest: String = ""

    private var searchJob: Job? = null

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

    fun playThisTrack(selectedTrack: Track) {
        tracksInteractor.playThisTrack(selectedTrack)
    }

    private fun loadSearchHistory(): ArrayList<Track> {
        return searchHistoryInteractor.getSearchHistory()
    }


    fun clearSearchField() {
        searchResultsList.clear()
        previousRequest = ""
        activityState.postValue(SearchState.Default(loadSearchHistory(), searchResultsList))
    }


    private fun search(searchRequest: String) {

        if (searchRequest.isNotBlank()) {

            searchResultsList.clear()
            unprocessedRequest = ""
            activityState.postValue(SearchState.Loading(searchResultsList))

            viewModelScope.launch {
                searchTracksInteractor
                    .searchTracks(searchRequest)
                    .collect { pair ->
                        processResult(pair.first, pair.second, searchRequest)
                    }
            }

        }

    }

    private fun processResult(foundTracks: List<Track>?, errorCode: Int?, searchRequest: String) {
        if (errorCode == 523) {
            activityState.postValue(SearchState.NoInternetConnectionError)
            unprocessedRequest = searchRequest
        } else if (foundTracks != null && foundTracks.isNotEmpty()) {
            searchResultsList.addAll(foundTracks)
            activityState.postValue(SearchState.ShowSearchResults(foundTracks as ArrayList<Track>))
        } else {
            activityState.postValue(SearchState.NoResultsFoundError)
        }
    }


    fun searchDebounce(searchRequest: String) {

        if (searchRequest != previousRequest) {
            previousRequest = searchRequest

            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                search(searchRequest)
            }
        }
    }

    fun repeatRequest() {
        search(unprocessedRequest)
    }

    override fun onCleared() {

    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }


}