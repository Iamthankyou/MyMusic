package com.example.mymusic.domain.usecase

import com.example.mymusic.data.repository.DiscoveryRepository
import com.example.mymusic.domain.model.Track
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiscoveryUseCase @Inject constructor(
    private val discoveryRepository: DiscoveryRepository
) {
    
    fun getTrendingTracks(limit: Int = 20, offset: Int = 0): Flow<List<Track>> {
        return discoveryRepository.getTrendingTracks(limit, offset)
    }
    
    fun getNewReleases(limit: Int = 20, offset: Int = 0): Flow<List<Track>> {
        return discoveryRepository.getNewReleases(limit, offset)
    }
    
    fun getTracksByGenre(genre: String, limit: Int = 20, offset: Int = 0): Flow<List<Track>> {
        return discoveryRepository.getTracksByGenre(genre, limit, offset)
    }
    
    fun getFeaturedContent(limit: Int = 10): Flow<List<Track>> {
        return discoveryRepository.getFeaturedContent(limit)
    }
}
