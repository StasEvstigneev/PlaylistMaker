package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.SettingsRepository



class App : Application() {

    var nightTheme: Boolean = false
    private lateinit var settingsRepository: SettingsRepository

    override fun onCreate() {
        super.onCreate()
        settingsRepository = Creator.getSettingsRepository(applicationContext)
        nightTheme = settingsRepository.getThemeSettings().nightMode
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
