package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.domain.settings.SettingsRepository
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    var nightTheme: Boolean = false

    override fun onCreate() {
        super.onCreate()

        val settingsRepository by inject<SettingsRepository>()

        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }

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