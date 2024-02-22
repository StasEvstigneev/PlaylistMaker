package com.example.playlistmaker.data.settings

import android.content.Context
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.model.ThemeSettings


class SettingsRepositoryImpl(context: Context) : SettingsRepository {

   private val localStorage = Creator.provideLocalStorage(context, SETTINGS_PREFERENCES)

    override fun getThemeSettings(): ThemeSettings {
        return when {
            localStorage.getSavedBooleanData(NIGHT_THEME) -> ThemeSettings.NIGHTON
            else -> ThemeSettings.NIGHTOFF
        }
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        localStorage.addBooleanData(NIGHT_THEME, settings.nightMode)
    }

    companion object{
        private const val SETTINGS_PREFERENCES = "settings_pref"
        private const val NIGHT_THEME = "night_theme"
    }
}