package com.example.playlistmaker.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.domain.createplaylist.models.Playlist
import com.example.playlistmaker.domain.player.model.AudioPlayerPlaylistsMessagesState
import com.example.playlistmaker.domain.player.model.AudioPlayerScreenState
import com.example.playlistmaker.presentation.player.AudioPlayerViewModel
import com.example.playlistmaker.utils.Formatter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import io.github.muddz.styleabletoast.StyleableToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudioPlayerFragment : Fragment() {

    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var playlistsAdapter: PlaylistsLinearLayoutAdapter
    private val viewModel by viewModel<AudioPlayerViewModel>()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.ivArrowReturn.setOnClickListener {
            findNavController().popBackStack()

        }

        viewModel.getScreenState().observe(viewLifecycleOwner) { screenState ->
            renderState(screenState)
        }

        val bottomSheetContainer = binding.playlistsBottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.ivAddToPlaylistButton.setOnClickListener {
            viewModel.getPlaylists()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }

                    else -> {
                        binding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        })

        binding.bottomSheetCreateButton.setOnClickListener {
            openCreatePlaylistFragment()
        }


        playlistsAdapter = PlaylistsLinearLayoutAdapter(ArrayList<Playlist>()) { playlist ->
            viewModel.addTrackToPlaylist(playlist)
        }

        binding.rvBottomSheet.adapter = playlistsAdapter
        binding.rvBottomSheet.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)


        viewModel.getPlaylistsState().observe(viewLifecycleOwner) { playlists ->
            playlistsAdapter.playlists = playlists
            playlistsAdapter.notifyDataSetChanged()
        }


        viewModel.getPlayerState().observe(viewLifecycleOwner) {
            binding.ivPlayButton.isEnabled = it.isPlayButtonEnabled
            if (it.isPlaying) {
                binding.ivPlayButton.setImageResource(R.drawable.ic_pause_button)
            } else {
                binding.ivPlayButton.setImageResource(R.drawable.ic_play_button)
            }
        }

        viewModel.getFavoriteStatus().observe(viewLifecycleOwner) { status ->

            if (status == true) {
                binding.ivAddToFavoritesButton.setImageResource(R.drawable.ic_is_favorite)
            } else {
                binding.ivAddToFavoritesButton.setImageResource(R.drawable.ic_is_not_favorite)
            }
        }

        viewModel.getPlaybackTimerLiveData().observe(viewLifecycleOwner) { progress ->
            binding.tvTrackPlaybackTimer.text = progress
        }

        viewModel.getPlaylistsMessage().observe(viewLifecycleOwner) { state ->
            processPlaylistMessages(state)

        }

        binding.ivPlayButton.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.ivAddToFavoritesButton.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

    }

    private fun renderState(screenState: AudioPlayerScreenState) {

        when (screenState) {

            is AudioPlayerScreenState.Loading -> {}

            is AudioPlayerScreenState.TrackIsLoaded -> {
                Glide.with(requireContext())
                    .load(screenState.track.artworkUrl512)
                    .placeholder(R.drawable.img_player_album_placeholder)
                    .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.audio_player_artWork_cornerRadius)))
                    .into(binding.ivPlayerArtwork)

                binding.tvTrackName.text = screenState.track.trackName
                binding.tvArtistName.text = screenState.track.artistName
                binding.tvTrackDuration.text = screenState.track.trackTime
                if (screenState.track.collectionName.isEmpty()) {
                    binding.trackAlbumGroup.isVisible = false
                } else {
                    binding.trackAlbumGroup.isVisible = true
                    binding.tvTrackAlbum.text = screenState.track.collectionName
                }
                binding.tvTrackYear.text =
                    Formatter.getYearFromReleaseDate(screenState.track.releaseDate)
                binding.tvTrackGenre.text = screenState.track.primaryGenreName
                binding.tvTrackCountry.text = screenState.track.country

            }

        }

    }

    private fun processPlaylistMessages(state: AudioPlayerPlaylistsMessagesState) {

        when (state) {
            is AudioPlayerPlaylistsMessagesState.Default -> {}
            is AudioPlayerPlaylistsMessagesState.TrackHasBeenAddedToPL -> {
                viewModel.getPlaylists()
                playlistsAdapter.notifyDataSetChanged()
                showToastAddedToPL(state.playlistName)

            }

            is AudioPlayerPlaylistsMessagesState.TrackHasNotBeenAddedToPL -> {
                showToastFailedToAddToPL(state.playlistName)
            }

        }

    }

    private fun showToastAddedToPL(playlistName: String) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        StyleableToast
            .makeText(
                requireContext(),
                "${getString(R.string.added_to_playlist)} $playlistName",
                R.style.CustomStyleableToast
            )
            .show()

        viewModel.setPlaylistsMessagesDefaultState()
    }


    private fun showToastFailedToAddToPL(playlistName: String) {
        StyleableToast
            .makeText(
                requireContext(),
                "${getString(R.string.track_is_already_in_PL)} $playlistName",
                R.style.CustomStyleableToast
            )
            .show()

        viewModel.setPlaylistsMessagesDefaultState()
    }


    private fun openCreatePlaylistFragment() {
        findNavController().navigate(R.id.action_audioPlayerFragment_to_createPlaylistFragment)

    }


    override fun onPause() {
        super.onPause()
        viewModel.onPauseControl()

    }

    override fun onResume() {
        super.onResume()
        viewModel.onResumeControl()
        viewModel.getPlaylists()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}