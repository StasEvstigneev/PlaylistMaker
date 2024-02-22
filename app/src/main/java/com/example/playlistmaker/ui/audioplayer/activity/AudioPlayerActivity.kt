package com.example.playlistmaker.ui.audioplayer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.utils.Formatter
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.player.model.AudioPlayerScreenState
import com.example.playlistmaker.domain.player.model.AudioPlayerStatus
import com.example.playlistmaker.ui.audioplayer.view_model.AudioPlayerViewModel


class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var viewModel: AudioPlayerViewModel
    private lateinit var binding: ActivityAudioPlayerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            AudioPlayerViewModel.getViewModelFactory(
                Creator.provideSearchHistoryRepository(
                    applicationContext,
                    Creator.provideGsonJsonConverter()
                )
            )
        )[AudioPlayerViewModel::class.java]

        binding.ivArrowReturn.setOnClickListener {
            this.finish()
        }

        viewModel.getScreenStateLiveData().observe(this) { screenState ->
            renderState(screenState)
        }

        viewModel.getPlayerStatusLiveData().observe(this) {status ->

            when (status) {
                AudioPlayerStatus.DEFAULT -> {
                    binding.ivPlayButton.isEnabled = status.term
                }
                AudioPlayerStatus.PREPARED -> {
                    binding.ivPlayButton.isEnabled = status.term
                    binding.ivPlayButton.setImageResource(R.drawable.ic_play_button)
                }
                AudioPlayerStatus.PLAYING -> {
                    binding.ivPlayButton.setImageResource(R.drawable.ic_pause_button)
                }
                AudioPlayerStatus.PAUSED -> {
                    binding.ivPlayButton.setImageResource(R.drawable.ic_play_button)
                }

            }
        }


        viewModel.getTrackPlaybackTimerLiveData().observe(this) { time ->
            binding.tvTrackPlaybackTimer.text = time
        }

        binding.ivPlayButton.setOnClickListener {
            viewModel.playbackControl()
        }

    }

    private fun renderState(screenState: AudioPlayerScreenState) {

        when (screenState) {
            is AudioPlayerScreenState.Content -> {
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
                binding.tvTrackYear.text = Formatter.getYearFromReleaseDate(screenState.track.releaseDate)
                binding.tvTrackGenre.text = screenState.track.primaryGenreName
                binding.tvTrackCountry.text = screenState.track.country
            }

            else -> false
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onResume() {
        super.onResume()
        viewModel.playbackControl()
    }



}