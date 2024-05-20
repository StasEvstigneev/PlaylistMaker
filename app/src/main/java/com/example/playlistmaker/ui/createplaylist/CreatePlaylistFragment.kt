package com.example.playlistmaker.ui.createplaylist


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.domain.createplaylist.models.CreatePlaylistState
import com.example.playlistmaker.presentation.createplaylist.CreatePlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.github.muddz.styleabletoast.StyleableToast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class CreatePlaylistFragment : Fragment() {

    private val viewModel by viewModel<CreatePlaylistViewModel>()

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!

    private lateinit var confirmExitDialog: MaterialAlertDialogBuilder

    private lateinit var titleTextWatcher: TextWatcher
    private lateinit var descriptionTextWatcher: TextWatcher

    private var title: String = ""
    private var image: Uri? = null

    private var showExitDialog: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getScreenState()
            .observe(viewLifecycleOwner) { screenState -> renderState(screenState) }


        viewModel.getImageState().observe(viewLifecycleOwner) { imageUri ->
            if (imageUri != null) {
                binding.addPlaylistCover.setImageURI(imageUri)
                image = imageUri
                showExitDialog = true

            } else {
                binding.addPlaylistCover.setImageResource(R.drawable.playlist_img_picker)
            }
        }

        confirmExitDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.exit_dialog_title)
            .setMessage(R.string.exit_dialog_message)
            .setPositiveButton(R.string.exit_dialog_quit) { dialog, which ->
                parentFragmentManager.popBackStack()
            }
            .setNeutralButton(R.string.exit_dialog_cancel) { dialog, which -> }


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


        titleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                viewModel.updateTitle(s.toString() ?: "")
            }

        }
        titleTextWatcher?.let { binding.titleInput.addTextChangedListener(it) }


        descriptionTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                viewModel.updateDescription(s.toString() ?: "")
            }

        }
        descriptionTextWatcher?.let { binding.descriptionInput.addTextChangedListener(it) }


        val pickImage =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    viewModel.updateImage(uri)

                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.image_not_chosen),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

        binding.addPlaylistCover.setOnClickListener {
            pickImage
                .launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        binding.buttonCreate.setOnClickListener {

            if (image != null) {
                saveImageToPrivateStorage(image!!, title)
            }

            viewModel.savePlaylist()
            parentFragmentManager.popBackStack()

            StyleableToast
                .makeText(
                    requireContext(),
                    "${getString(R.string.playlist)} $title ${getString(R.string.created)}",
                    R.style.CustomStyleableToast
                )
                .show()
        }

    }


    private fun saveImageToPrivateStorage(uri: Uri, imageName: String) {

        val filePath =
            File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                COVERS_ALBUM
            )

        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, "$imageName.jpg")

        viewModel.updateImagePath(file.path)

        val inputStream = requireContext().contentResolver.openInputStream(uri)

        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        titleTextWatcher?.let { binding.titleInput.removeTextChangedListener(it) }
        descriptionTextWatcher?.let { binding.descriptionInput.removeTextChangedListener(it) }
        _binding = null

    }


    private fun renderState(state: CreatePlaylistState) {

        when (state) {
            is CreatePlaylistState.Loading -> {}
            is CreatePlaylistState.DataUpdated -> {
                if (state.title.isNullOrEmpty()) {
                    binding.buttonCreate.isEnabled = false
                } else {
                    binding.buttonCreate.isEnabled = true
                    title = state.title
                }

                showExitDialog =
                    !(state.title.isNullOrEmpty() && state.description.isNullOrEmpty())
            }
        }

    }


    private fun leaveFragment() {
        if (showExitDialog) {
            confirmExitDialog.show()

        } else {
            findNavController().popBackStack()

        }
    }


    companion object {
        const val COVERS_ALBUM = "Playlistmaker covers"
    }

}