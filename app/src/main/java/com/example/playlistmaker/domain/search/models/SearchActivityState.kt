package com.example.playlistmaker.domain.search.models

//enum class SearchActivityState {
//    DEFAULT,
//    SHOWSEARCHHISTORY,
//    LOADING,
//    SHOWRESULTS,
//    NORESULTSFOUNDERROR,
//    CONNECTIONERROR
//}

sealed class SearchActivityState {
    class Default: SearchActivityState()

    class Loading: SearchActivityState()
    class ShowResults: SearchActivityState()
    class NoResultsFoundError: SearchActivityState()
    class NoInternetConnectionError: SearchActivityState()

}