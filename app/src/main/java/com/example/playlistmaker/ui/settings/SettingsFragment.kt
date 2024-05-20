package com.example.playlistmaker.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<SettingsViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switcherNightTheme.isChecked =
            (getActivity()?.getApplicationContext() as App).nightTheme

        binding.switcherNightTheme.setOnCheckedChangeListener { switcher, isChecked ->
            (getActivity()?.getApplicationContext() as App).switchNightTheme(isChecked)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}