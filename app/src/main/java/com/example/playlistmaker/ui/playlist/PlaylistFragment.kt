package com.example.playlistmaker.ui.playlist


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.playlist.BottomSheetState
import com.example.playlistmaker.domain.playlist.PlaylistScreenState
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.playlist.PlaylistViewModel
import com.example.playlistmaker.ui.mediateka.fragments.playlists.PlaylistsFragment.Companion.PLAYLIST_ID
import com.example.playlistmaker.utils.Formatter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistFragment : Fragment(), RecyclerViewClickInterface {

    private val viewModel by viewModel<PlaylistViewModel>()

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private var playlistId: Int? = null

    private lateinit var bundle: Bundle

    private lateinit var adapter: PlaylistAdapter
    private lateinit var playlistBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var menuBottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private var trackToRemove: Track? = null
    private lateinit var removeTrackDialog: MaterialAlertDialogBuilder
    private lateinit var removePlaylistDialog: MaterialAlertDialogBuilder


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistId = arguments?.getInt(PLAYLIST_ID)
        if (playlistId != null) {
            viewModel.getPlaylistById(playlistId!!)
        }

        viewModel.getPlaylistState().observe(viewLifecycleOwner) { playlist ->
            viewModel.updatePlaylist(playlist)
            bundle = bundleOf(
                ID to playlist.id,
                TITLE to playlist.title,
                DESCRIPTION to playlist.description,
                COVER_PATH to playlist.coverPath
            )
        }


        viewModel.getScreenState().observe(viewLifecycleOwner) { state ->
            renderScreenState(state)
        }

        val playlistBottomSheetContainer = binding.playlistBottomSheet
        playlistBottomSheetBehavior = BottomSheetBehavior.from(playlistBottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED

            val displayHeight = resources.displayMetrics.heightPixels
            Log.d("Get display height", "Height = $displayHeight")

            setPeekHeight(
                getPlaylistBottomSheetPeekHeight(
                    resources
                        .displayMetrics
                        .heightPixels
                )
            )
        }


        val menuBottomSheetContainer = binding.menuBottomSheet
        menuBottomSheetBehavior = BottomSheetBehavior.from(menuBottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN

            setPeekHeight(
                (resources
                    .displayMetrics
                    .heightPixels * RATIO_048).toInt()
            )

        }

        menuBottomSheetBehavior.addBottomSheetCallback(object :
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


        viewModel.getBottomSheetState().observe(viewLifecycleOwner) { state ->
            renderBottomSheetState(state)
        }

        adapter = PlaylistAdapter(ArrayList<Track>(), this)

        binding.tracklist.adapter = adapter
        binding.tracklist.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.toolbar.navigationIcon!!.setTint(requireContext().resources.getColor(R.color.black))

        removeTrackDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.confirm_track_removal)
            .setPositiveButton(R.string.remove) { dialog, which ->
                viewModel.deleteTrackFromPlaylist(trackToRemove!!)
                viewModel.updatePlaylistData()
                adapter.notifyDataSetChanged()
                overlayInvisible()
            }
            .setNegativeButton(R.string.exit_dialog_cancel) { dialog, which ->
                trackToRemove = null
                overlayInvisible()
            }

        removePlaylistDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.confirm_playlist_removal)
            .setPositiveButton(R.string.remove) { dialog, which ->
                viewModel.deletePlaylist(adapter.list)
                overlayInvisible()
                findNavController().popBackStack()
            }
            .setNegativeButton(R.string.exit_dialog_cancel) { dialog, which ->
                overlayInvisible()
            }


        binding.shareIcon.setOnClickListener {
            sharePlaylist()
        }


        binding.editIcon.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.menuShare.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            sharePlaylist()
        }

        binding.menuEdit.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            findNavController().navigate(
                R.id.action_playlistFragment_to_editPlaylistFragment,
                bundle
            )
        }


        binding.menuRemovePlaylist.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            removePlaylistDialog.show()

        }

    }

    private fun sharePlaylist() {
        if (!adapter.list.isNullOrEmpty()) {
            viewModel.sharePlaylist(adapter.list)
        } else {
            Toast.makeText(
                requireContext(),
                R.string.no_tracks_to_share,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun renderScreenState(state: PlaylistScreenState) {

        when (state) {
            is PlaylistScreenState.Loading -> {}
            is PlaylistScreenState.PlaylistData -> {
                binding.playlistTitle.text = state.title
                binding.plTitle.text = state.title

                if (state.description.isNullOrEmpty()) {
                    binding.playlistDescription.isVisible = false
                } else {
                    binding.playlistDescription.isVisible = true
                    binding.playlistDescription.text = state.description
                }

                Glide.with(this)
                    .load(state.coverPath)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.img_player_album_placeholder)
                    .centerCrop()
                    .into(binding.cover)

                Glide.with(this)
                    .load(state.coverPath)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.img_player_album_placeholder)
                    .centerCrop()
                    .into(binding.plCover)

                binding.playlistTime.text = Formatter
                    .playlistTotalDurationFormatter(state.totalDuration, requireContext())

                binding.playlistTracksQty.text = Formatter
                    .playlistTracksQuantityFormatter(state.tracksQuantity, requireContext())

                binding.plTracksQuantity.text = binding.playlistTracksQty.text

            }
        }
    }

    private fun renderBottomSheetState(state: BottomSheetState) {
        when (state) {
            is BottomSheetState.NoTracks -> {
                binding.playlistBottomSheet.isVisible = false
                binding.noTracksInPlaylist.isVisible = true
            }

            is BottomSheetState.TracksAvailable -> {
                binding.playlistBottomSheet.isVisible = true
                binding.noTracksInPlaylist.isVisible = false
                adapter.list = state.tracks
                adapter.notifyDataSetChanged()
            }
        }
    }


    override fun onItemCLick(track: Track) {
        viewModel.playThisTrack(track)
        findNavController().navigate(R.id.action_playlistFragment_to_audioPlayerFragment)
    }

    override fun onLongItemClick(track: Track) {
        trackToRemove = track
        binding.overlay.isVisible = true
        removeTrackDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaylistById(playlistId!!)

    }

    private fun overlayInvisible() {
        binding.overlay.isVisible = false
    }

    private fun getPlaylistBottomSheetPeekHeight(displayHeight: Int): Int {

        return when (displayHeight) {
            in 0..1500 -> (displayHeight * RATIO_012).toInt()
            in 1501..1900 -> (displayHeight * RATIO_015).toInt()
            in 1901..2100 -> (displayHeight * RATIO_020).toInt()
            in 2101..2900 -> (displayHeight * RATIO_026).toInt()
            else -> (displayHeight * RATIO_028).toInt()

        }

    }

    companion object {
        const val ID = "id"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val COVER_PATH = "coverPath"
        private const val RATIO_012 = 0.12
        private const val RATIO_015 = 0.15
        private const val RATIO_020 = 0.20
        private const val RATIO_026 = 0.26
        private const val RATIO_028 = 0.28
        private const val RATIO_048 = 0.48

    }

}