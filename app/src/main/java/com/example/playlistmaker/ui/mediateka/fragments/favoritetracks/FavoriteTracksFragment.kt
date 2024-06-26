package com.example.playlistmaker.ui.mediateka.fragments.favoritetracks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.domain.mediateka.models.FavoriteTracksState
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.mediateka.FavoriteTracksViewModel
import com.example.playlistmaker.ui.search.TracksAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {

    private val viewModel by viewModel<FavoriteTracksViewModel>()

    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TracksAdapter
    private var isClickAllowed: Boolean = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavoriteTracks()

        viewModel.observeScreenState().observe(viewLifecycleOwner) { state ->
            renderState(state)
        }


        adapter = TracksAdapter(ArrayList<Track>()) {
            if (clickDebounce()) {
                processClickedTrack(it)
            }
        }
        binding.rvFavoriteTracks.adapter = adapter
        binding.rvFavoriteTracks.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            true
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavoriteTracks()
        adapter.notifyDataSetChanged()
        isClickAllowed = true
    }


    private fun renderState(state: FavoriteTracksState) {
        when (state) {
            is FavoriteTracksState.Loading -> showProgressBar()
            is FavoriteTracksState.NoFavoriteTracks -> showEmptyFavoritesPlaceholder()
            is FavoriteTracksState.FavoriteTracks -> showContent(state.favoriteTracks)
        }
    }


    private fun showEmptyFavoritesPlaceholder() {
        binding.tvNoFavoriteTracksPlaceholder.isVisible = true
        binding.favTracksProgressBar.isVisible = false
        binding.rvFavoriteTracks.isVisible = false
    }

    private fun showProgressBar() {
        binding.favTracksProgressBar.isVisible = true
        binding.tvNoFavoriteTracksPlaceholder.isVisible = false
        binding.rvFavoriteTracks.isVisible = false
    }

    private fun showContent(tracks: List<Track>) {
        binding.favTracksProgressBar.isVisible = false
        binding.rvFavoriteTracks.isVisible = true
        binding.tvNoFavoriteTracksPlaceholder.isVisible = false
        adapter.list = tracks as ArrayList<Track>
        adapter.notifyDataSetChanged()
    }


    private fun clickDebounce(): Boolean {
        val clickStatus = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(ON_TRACK_CLICK_DELAY)
                isClickAllowed = true
            }
        }
        return clickStatus
    }

    private fun processClickedTrack(track: Track) {
        viewModel.playThisTrack(track)
        findNavController().navigate(R.id.action_mediatekaFragment_to_audioPlayerFragment)

    }


    companion object {
        fun newInstance() = FavoriteTracksFragment()
        private const val ON_TRACK_CLICK_DELAY = 2000L

    }


}