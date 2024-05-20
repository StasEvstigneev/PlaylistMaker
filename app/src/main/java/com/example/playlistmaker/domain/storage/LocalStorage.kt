package com.example.playlistmaker.domain.storage

interface LocalStorage {

    fun addStringData(key: String, string: String)

    fun getSavedStringData(key: String): String

    fun addBooleanData(key: String, boolean: Boolean)

    fun getSavedBooleanData(key: String): Boolean

    fun clearData()


}