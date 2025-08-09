package com.example.mymusic.domain.usecase

import com.example.mymusic.domain.model.Track
import com.example.mymusic.domain.repository.TrackRepository
import javax.inject.Inject

class GetTrendingTracksUseCase @Inject constructor(
    private val trackRepository: TrackRepository
) {
    suspend operator fun invoke(limit: Int, offset: Int): List<Track> {
        return trackRepository.getTrendingTracks(limit, offset)
    }
}


