package com.example.playlistmaker.ui.settings.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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