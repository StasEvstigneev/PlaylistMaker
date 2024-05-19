package com.example.playlistmaker.data.settings

import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import com.example.playlistmaker.domain.storage.LocalStorage


class SettingsRepositoryImpl(private val localStorage: LocalStorage) : SettingsRepository {


    override fun getThemeSettings(): ThemeSettings {
        return when {
            localStorage.getSavedBooleanData(NIGHT_THEME) -> ThemeSettings.NIGHTON
            else -> ThemeSettings.NIGHTOFF
        }
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        localStorage.addBooleanData(NIGHT_THEME, settings.nightMode)
    }

    companion object {
        private const val NIGHT_THEME = "night_theme"
    }
}