package com.example.mymusic.data.repository

import android.util.Log
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
        Log.d("DiscoveryRepository", "Fetching trending tracks: limit=$limit, offset=$offset")
        val response = service.getDiscoveryTrendingTracks(limit = limit, offset = offset)
        Log.d("DiscoveryRepository", "Trending response received: headers=${response.headers}, results count=${response.results.size}")
        
        if (response.results.isEmpty()) {
            Log.w("DiscoveryRepository", "No trending tracks found in response")
        }
        
        val tracks = response.results.map { trackDto ->
            try {
                trackMapper.fromDto(trackDto)
            } catch (e: Exception) {
                Log.e("DiscoveryRepository", "Error mapping track: ${trackDto.id}", e)
                Track(
                    id = trackDto.id,
                    title = trackDto.name,
                    artist = trackDto.artist_name,
                    durationMs = 0L,
                    artworkUrl = trackDto.image.ifEmpty { trackDto.album_image },
                    audioUrl = trackDto.audio,
                    isDownloadable = trackDto.audiodownload.isNotEmpty()
                )
            }
        }
        Log.d("DiscoveryRepository", "Mapped ${tracks.size} trending tracks")
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
