package com.example.playlistmaker.data.storage


import android.content.SharedPreferences
import com.example.playlistmaker.domain.storage.LocalStorage

class LocalStorageImpl(private val sharedPreferences: SharedPreferences): LocalStorage {


    override fun addStringData(key: String, string: String) {
        sharedPreferences.edit().putString(key, string).apply()
    }

    override fun getSavedStringData(key: String): String {
        return sharedPreferences.getString(key, null) ?: ""
    }

    override fun addBooleanData(key: String, boolean: Boolean) {
        sharedPreferences.edit().putBoolean(key, boolean).apply()
    }

    override fun getSavedBooleanData(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    override fun clearData() {
        sharedPreferences.edit().clear().apply()
    }

}