package com.example.playlistmaker.ui.mediateka.fragments.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.createplaylist.models.Playlist
import com.example.playlistmaker.domain.mediateka.models.PlaylistsState
import com.example.playlistmaker.presentation.mediateka.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val viewModel by viewModel<PlaylistsViewModel>()

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PlaylistsGridLayoutAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewPlaylists.layoutManager =
            GridLayoutManager(requireContext(), GRID_LAYOUT_SPAN)

        viewModel.observeScreenState().observe(viewLifecycleOwner) { screenState ->
            renderState(screenState)

        }

        adapter = PlaylistsGridLayoutAdapter(ArrayList<Playlist>())

        binding.recyclerViewPlaylists.adapter = adapter


        binding.createPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediatekaFragment_to_createPlaylistFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaylists()
    }


    private fun renderState(state: PlaylistsState) {

        when (state) {
            is PlaylistsState.Loading -> {
                binding.playlistsProgressBar.isVisible = true
                binding.tvNoPlaylistsPlaceholder.isVisible = false
                binding.recyclerViewPlaylists.isVisible = false
            }

            is PlaylistsState.NoPlaylistAvailable -> {
                binding.playlistsProgressBar.isVisible = false
                binding.tvNoPlaylistsPlaceholder.isVisible = true
                binding.recyclerViewPlaylists.isVisible = false
            }

            is PlaylistsState.PlaylistsAvailable -> {
                adapter.playlists = state.playlists
                adapter.notifyDataSetChanged()
                binding.playlistsProgressBar.isVisible = false
                binding.tvNoPlaylistsPlaceholder.isVisible = false
                binding.recyclerViewPlaylists.isVisible = true

            }

        }

    }


    companion object {
        fun newInstance() = PlaylistsFragment()
        private const val GRID_LAYOUT_SPAN = 2

    }

}