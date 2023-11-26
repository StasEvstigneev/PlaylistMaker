package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val SETTINGS_PREFERENCES = "settings_pref"
const val NIGHT_THEME = "night_theme"

class App : Application() {

    var nightTheme: Boolean = false
    lateinit var settingsSharedPrefs: SharedPreferences


    override fun onCreate() {
        super.onCreate()

        settingsSharedPrefs = getSharedPreferences(SETTINGS_PREFERENCES, MODE_PRIVATE)
        nightTheme = settingsSharedPrefs.getBoolean(NIGHT_THEME, false)
        switchNightTheme(nightTheme)

    }

    fun switchNightTheme(nightThemeEnabled: Boolean) {
        nightTheme = nightThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (nightThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}
