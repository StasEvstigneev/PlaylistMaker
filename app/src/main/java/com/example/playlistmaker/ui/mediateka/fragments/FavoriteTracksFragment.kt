package com.example.playlistmaker.ui.mediateka.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.domain.mediateka.models.FavoriteTracksState
import com.example.playlistmaker.ui.mediateka.view_model.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {

    private val viewModel by viewModel<FavoriteTracksViewModel>()

    private lateinit var binding: FragmentFavoriteTracksBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeScreenState().observe(viewLifecycleOwner) {
            when (it) {
                is FavoriteTracksState.NoFavoriteTracks -> showEmptyFavoritesPlaceholder()
                is FavoriteTracksState.FavoriteTracks -> hideEmptyFavoritesPlaceholder()
            }

        }

    }


    private fun showEmptyFavoritesPlaceholder() {
        binding.tvNoFavoriteTracksPlaceholder.isVisible = true
    }

    private fun hideEmptyFavoritesPlaceholder() {
        binding.tvNoFavoriteTracksPlaceholder.isVisible = false
    }

    companion object {
        fun newInstance() = FavoriteTracksFragment()

    }


}