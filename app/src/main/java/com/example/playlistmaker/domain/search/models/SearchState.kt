package com.example.playlistmaker.domain.search.models


sealed class SearchState {
    data class Default(val searchHistory: ArrayList<Track>, val searchResults: ArrayList<Track>): SearchState()

    data class Loading(val searchResults: ArrayList<Track>): SearchState()
    data class ShowSearchResults(val searchResults: ArrayList<Track>): SearchState()

    object NoResultsFoundError: SearchState()
    object NoInternetConnectionError: SearchState()

}