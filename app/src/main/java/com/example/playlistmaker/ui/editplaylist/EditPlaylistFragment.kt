package com.example.playlistmaker.ui.editplaylist


import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.editplaylist.EditPlaylistViewModel
import com.example.playlistmaker.ui.createplaylist.CreatePlaylistFragment
import com.example.playlistmaker.ui.playlist.PlaylistFragment.Companion.COVER_PATH
import com.example.playlistmaker.ui.playlist.PlaylistFragment.Companion.DESCRIPTION
import com.example.playlistmaker.ui.playlist.PlaylistFragment.Companion.ID
import com.example.playlistmaker.ui.playlist.PlaylistFragment.Companion.TITLE
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : CreatePlaylistFragment() {

    override val viewModel by viewModel<EditPlaylistViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.title = getString(R.string.edit)
        binding.buttonCreate.text = getString(R.string.save)

        Glide.with(this)
            .load(arguments?.getString(COVER_PATH))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .centerCrop()
            .placeholder(R.drawable.playlist_img_picker)
            .into(binding.addPlaylistCover)


        binding.textInputLayoutTitle.editText?.setText(arguments?.getString(TITLE)!!)
        binding.textInputLayoutDescription.editText?.setText(arguments?.getString(DESCRIPTION))


        viewModel.updateTitle(arguments?.getString(TITLE)!!)
        viewModel.updateDescription(arguments?.getString(DESCRIPTION))
        viewModel.updatePlaylistId(arguments?.getInt(ID)!!)
        viewModel.updateImagePath(arguments?.getString(COVER_PATH))


        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    leaveFragment()
                }
            })


        binding.toolbar.setNavigationOnClickListener {
            leaveFragment()
        }

        binding.buttonCreate.setOnClickListener {
            if (image != null) {
                saveImageToPrivateStorage(image!!, title)
            }
            viewModel.updatePlaylist()
            parentFragmentManager.popBackStack()
        }

    }

    private fun leaveFragment() {
        findNavController().popBackStack()
    }

}