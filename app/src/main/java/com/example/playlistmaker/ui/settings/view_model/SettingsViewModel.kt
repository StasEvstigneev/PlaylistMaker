package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import com.example.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsRepository: SettingsRepository
): ViewModel() {

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    fun updateThemeSetting(nightMode: Boolean) {
        if (nightMode) settingsRepository.updateThemeSetting(ThemeSettings.NIGHTON)
        else settingsRepository.updateThemeSetting(ThemeSettings.NIGHTOFF)

    }


    companion object {
        fun getViewModelFactory(sharingInteractor: SharingInteractor, settingsRepository: SettingsRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(
                        sharingInteractor,
                        settingsRepository
                    ) as T
                }
            }

    }


}