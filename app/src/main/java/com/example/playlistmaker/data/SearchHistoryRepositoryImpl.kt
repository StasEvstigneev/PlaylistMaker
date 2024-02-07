package com.example.playlistmaker.data

import android.content.Context
import com.example.playlistmaker.domain.Constants.SHARED_PREFS_NAME
import com.example.playlistmaker.domain.SearchHistoryRepository



class SearchHistoryRepositoryImpl(context: Context): SearchHistoryRepository {

    override val sharedPreferences = context
        .getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

}