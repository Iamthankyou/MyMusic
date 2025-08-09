package com.example.mymusic.data.repository

import com.example.mymusic.data.mapper.TrackMapper
import com.example.mymusic.data.remote.JamendoTracksService
import com.example.mymusic.domain.model.Track
import com.example.mymusic.domain.repository.TrackRepository
import javax.inject.Inject

class TrackRepositoryImpl @Inject constructor(
    private val service: JamendoTracksService
) : TrackRepository {
    override suspend fun getTrendingTracks(limit: Int, offset: Int): List<Track> {
        val response = service.getTrendingTracks(limit = limit, offset = offset)
        return response.results.map { TrackMapper.fromDto(it) }
    }
}


