package com.example.playlistmaker.ui.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.utils.Formatter
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.player.model.AudioPlayerScreenState
import com.example.playlistmaker.presentation.player.AudioPlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
    private val viewModel by viewModel<AudioPlayerViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivArrowReturn.setOnClickListener {
            this.finish()
        }

        viewModel.getScreenState().observe(this) { screenState ->
            renderState(screenState)
        }

        viewModel.getPlayerState().observe(this) {
            binding.ivPlayButton.isEnabled = it.isPlayButtonEnabled
            if (it.isPlaying) {
                binding.ivPlayButton.setImageResource(R.drawable.ic_pause_button)
            } else {
                binding.ivPlayButton.setImageResource(R.drawable.ic_play_button)
            }
        }

        viewModel.getFavoriteStatus().observe(this) { status ->

            if (status == true) {
                binding.ivAddToFavoritesButton.setImageResource(R.drawable.ic_is_favorite)
            } else {
                binding.ivAddToFavoritesButton.setImageResource(R.drawable.ic_is_not_favorite)
            }
        }

        viewModel.getPlaybackTimerLiveData().observe(this) { progress ->
            binding.tvTrackPlaybackTimer.text = progress
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
            is AudioPlayerScreenState.TrackIsLoaded -> {
                Glide.with(applicationContext)
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

            else -> {}
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.onPauseControl()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResumeControl()
    }


}