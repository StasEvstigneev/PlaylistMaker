package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.SearchTracksInteractor
import com.example.playlistmaker.domain.search.SearchTracksRepository
import com.example.playlistmaker.domain.search.models.Resource
import com.example.playlistmaker.domain.search.models.Track
import java.util.concurrent.Executors

class SearchTracksInteractorImpl(private val repository: SearchTracksRepository): SearchTracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: SearchTracksInteractor.TracksConsumer) {
        executor.execute {
            when(val resource: Resource<List<Track>> = repository.searchTracks(expression)) {
                is Resource.Success -> {consumer.consume(resource.data, null)}
                is Resource.Error -> {consumer.consume(null, resource.errorCode)}

            }
        }
    }
}