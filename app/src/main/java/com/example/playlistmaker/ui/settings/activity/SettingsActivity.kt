package com.example.playlistmaker.ui.settings.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, SettingsViewModel.getViewModelFactory(
                Creator.provideSharingInteractor(
                    Creator.provideExternalNavigator(this),
                    this),
                Creator.provideSettingsInteractor(applicationContext)
            )
        )[SettingsViewModel::class.java]

        binding.returnButton.setOnClickListener {
            this.finish()
        }

        binding.switcherNightTheme.isChecked = (applicationContext as App).nightTheme
        binding.switcherNightTheme.setOnCheckedChangeListener { switcher, isChecked ->
            (applicationContext as App).switchNightTheme(isChecked)
            viewModel.updateThemeSetting(isChecked)

        }

        binding.ivShareSettings.setOnClickListener {
            viewModel.shareApp()
        }

        binding.ivSupportSettings.setOnClickListener {
            viewModel.openSupport()
        }

        binding.ivUserAgreementSettings.setOnClickListener {
            viewModel.openTerms()
        }

    }
}