package com.example.mymusic.data.repository

import com.example.mymusic.data.repository.AggregatedMusicRepository
import com.example.mymusic.domain.model.Track
import com.example.mymusic.domain.repository.TrackRepository
import javax.inject.Inject

class TrackRepositoryImpl @Inject constructor(
    private val aggregatedRepository: AggregatedMusicRepository
) : TrackRepository {
    override suspend fun getTrendingTracks(limit: Int, offset: Int): List<Track> {
        return aggregatedRepository.getTrendingTracks(limit = limit, offset = offset)
    }
}


