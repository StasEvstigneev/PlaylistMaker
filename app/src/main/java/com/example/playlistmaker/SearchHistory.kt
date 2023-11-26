package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SearchHistory(sharedPreferences: SharedPreferences) {

    var sharedPreferences = sharedPreferences


    fun getSearchHistory(): ArrayList<Track> {
        val json =
            sharedPreferences.getString(SEARCH_HISTORY_KEY, null) ?: return ArrayList<Track>()
        return Gson().fromJson(json, object : TypeToken<ArrayList<Track>>() {}.type)
    }

    fun addNewElement(element: Track, adapter: SearchResultsAdapter): ArrayList<Track> {
        val updatedList = adapter.searchResults
        val iterator = updatedList.iterator()
        while (iterator.hasNext()) {
            val track = iterator.next()
            if (track.trackId == element.trackId) {
                val removedIndex = updatedList.indexOf(track)
                iterator.remove()
                adapter.notifyItemRemoved(removedIndex)
            }
        }


        if (updatedList.size >= SEARCH_HISTORY_ITEMS_LIMIT) {
            updatedList.removeFirst()
            adapter.notifyItemRemoved(0)

        }

        updatedList.add(element)
        adapter.notifyItemInserted(updatedList.size - 1)
        adapter.notifyItemRangeChanged(0, updatedList.size - 1)


        val json = Gson().toJson(updatedList)
        sharedPreferences.edit().putString(SEARCH_HISTORY_KEY, json).apply()

        return updatedList


    }

    fun clearSearchHistory() {
        sharedPreferences.edit().clear().apply()
    }


}