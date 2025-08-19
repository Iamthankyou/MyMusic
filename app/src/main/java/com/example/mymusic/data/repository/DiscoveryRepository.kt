package com.example.mymusic.data.repository

import android.util.Log
import com.example.mymusic.data.mapper.TrackMapper
import com.example.mymusic.data.remote.JamendoTracksService
import com.example.mymusic.data.repository.AggregatedMusicRepository
import com.example.mymusic.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiscoveryRepository @Inject constructor(
    private val service: JamendoTracksService,
    private val trackMapper: TrackMapper,
    private val aggregatedRepository: AggregatedMusicRepository
) {
    
    fun getTrendingTracks(limit: Int = 20, offset: Int = 0): Flow<List<Track>> = flow {
        Log.d("DiscoveryRepository", "Fetching trending tracks (aggregated): limit=$limit, offset=$offset")
        val tracks = aggregatedRepository.getTrendingTracks(limit = limit, offset = offset)
        emit(tracks)
    }
    
    fun getNewReleases(limit: Int = 20, offset: Int = 0): Flow<List<Track>> = flow {
        Log.d("DiscoveryRepository", "Fetching new releases: limit=$limit, offset=$offset")
        val response = service.getNewReleases(limit = limit, offset = offset)
        Log.d("DiscoveryRepository", "New releases response: ${response.results.size} tracks")
        val tracks = response.results.map { trackDto ->
            trackMapper.fromDto(trackDto)
        }
        emit(tracks)
    }
    
    fun getTracksByGenre(genre: String, limit: Int = 20, offset: Int = 0): Flow<List<Track>> = flow {
        Log.d("DiscoveryRepository", "Fetching tracks by genre: $genre, limit=$limit, offset=$offset")
        val response = service.getTracksByGenre(
            genre = genre,
            limit = limit,
            offset = offset
        )
        Log.d("DiscoveryRepository", "Genre response: ${response.results.size} tracks")
        val tracks = response.results.map { trackDto ->
            trackMapper.fromDto(trackDto)
        }
        emit(tracks)
    }
    
    fun getFeaturedTracks(limit: Int = 10, offset: Int = 0): Flow<List<Track>> = flow {
        Log.d("DiscoveryRepository", "Fetching featured tracks: limit=$limit, offset=$offset")
        val response = service.getFeaturedTracks(limit = limit, offset = offset)
        Log.d("DiscoveryRepository", "Featured response: ${response.results.size} tracks")
        val tracks = response.results.map { trackDto ->
            trackMapper.fromDto(trackDto)
        }
        emit(tracks)
    }
}
