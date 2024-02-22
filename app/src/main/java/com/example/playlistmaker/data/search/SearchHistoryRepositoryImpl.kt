package com.example.playlistmaker.data.search

import android.content.Context
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.GsonJsonConverter
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.models.Track


class SearchHistoryRepositoryImpl(
    context: Context,
    gsonJsonConverter: GsonJsonConverter
): SearchHistoryRepository {

    private val localStorage = Creator
        .provideLocalStorage(
            context,
            SHARED_PREFS_NAME)

    private val getSearchHistoryUseCase = Creator
        .provideGetSearchHistoryUseCase(
            localStorage,
            gsonJsonConverter)

    private val addElementToSearchHistoryUseCase = Creator
        .provideAddElementToSearchHistoryUseCase(
            localStorage,
            getSearchHistoryUseCase,
            gsonJsonConverter)

    private val clearSearchHistoryUseCase = Creator
        .provideClearSearchHistoryUseCase(localStorage)

    private val selectTrackForPlayerUseCase = Creator
        .provideSelectTrackForPlayerUseCase(
            localStorage,
            gsonJsonConverter)

    private val receiveTrackInPlayerUseCase = Creator
        .provideReceiveTrackInPlayerUseCase(
            localStorage,
            gsonJsonConverter)


    override fun getSearchHistory(): ArrayList<Track> {
        return getSearchHistoryUseCase.execute(SEARCH_HISTORY_KEY)
    }

    override fun addElementToSearchHistory(newTrack: Track) {
        addElementToSearchHistoryUseCase
            .execute(
                SEARCH_HISTORY_KEY,
                newTrack,
                SEARCH_HISTORY_ITEMS_LIMIT)
    }

    override fun clearSearchHistory() {
        clearSearchHistoryUseCase.execute()
    }

    override fun selectTrackForPlayer(selectedTrack: Track) {
        selectTrackForPlayerUseCase.execute(SELECTED_TRACK_KEY, selectedTrack)
    }

    override fun receiveTackInPlayer(): Track {
        return receiveTrackInPlayerUseCase.execute(SELECTED_TRACK_KEY)
    }


    companion object{

        private const val SHARED_PREFS_NAME = "SharedPrefs"
        private const val SEARCH_HISTORY_KEY = "SearchHistoryKey"
        private const val SEARCH_HISTORY_ITEMS_LIMIT: Int = 10
        private const val SELECTED_TRACK_KEY = "TrackToPlay"

    }

}