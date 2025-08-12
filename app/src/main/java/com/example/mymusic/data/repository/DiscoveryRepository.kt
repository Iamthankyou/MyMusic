package com.example.mymusic.data.repository

import com.example.mymusic.data.mapper.TrackMapper
import com.example.mymusic.data.remote.JamendoTracksService
import com.example.mymusic.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiscoveryRepository @Inject constructor(
    private val service: JamendoTracksService,
    private val trackMapper: TrackMapper
) {
    
    fun getTrendingTracks(limit: Int = 20, offset: Int = 0): Flow<List<Track>> = flow {
        try {
            val response = service.getTrendingTracks(
                limit = limit,
                offset = offset
            )
            val tracks = response.results.map { trackDto ->
                trackMapper.fromDto(trackDto)
            }
            emit(tracks)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
    
    fun getNewReleases(limit: Int = 20, offset: Int = 0): Flow<List<Track>> = flow {
        try {
            val response = service.getNewReleases(
                limit = limit,
                offset = offset
            )
            val tracks = response.results.map { trackDto ->
                trackMapper.fromDto(trackDto)
            }
            emit(tracks)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
    
    fun getTracksByGenre(genre: String, limit: Int = 20, offset: Int = 0): Flow<List<Track>> = flow {
        try {
            val response = service.searchTracks(
                query = "genre:$genre",
                limit = limit,
                offset = offset
            )
            val tracks = response.results.map { trackDto ->
                trackMapper.fromDto(trackDto)
            }
            emit(tracks)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
    
    fun getFeaturedContent(limit: Int = 10): Flow<List<Track>> = flow {
        try {
            // For now, use trending tracks as featured content
            val response = service.getTrendingTracks(
                limit = limit,
                offset = 0
            )
            val tracks = response.results.map { trackDto ->
                trackMapper.fromDto(trackDto)
            }
            emit(tracks)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
}
