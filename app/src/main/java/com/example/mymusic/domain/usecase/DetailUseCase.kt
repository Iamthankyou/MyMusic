package com.example.mymusic.domain.usecase

import com.example.mymusic.data.repository.DetailRepository
import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.model.Artist
import com.example.mymusic.domain.model.Track
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailUseCase @Inject constructor(
    private val detailRepository: DetailRepository
) {
    
    fun getTrackDetail(trackId: String): Flow<Track?> {
        return detailRepository.getTrackDetail(trackId)
    }
    
    fun getArtistDetail(artistId: String): Flow<Artist?> {
        return detailRepository.getArtistDetail(artistId)
    }
    
    fun getArtistTracks(artistId: String, limit: Int = 20, offset: Int = 0): Flow<List<Track>> {
        return detailRepository.getArtistTracks(artistId, limit, offset)
    }
    
    fun getAlbumDetail(albumId: String): Flow<Album?> {
        return detailRepository.getAlbumDetail(albumId)
    }
    
    fun getRelatedTracks(trackId: String, limit: Int = 10): Flow<List<Track>> {
        return detailRepository.getRelatedTracks(trackId, limit)
    }
    
    fun getRelatedArtists(artistId: String, limit: Int = 10): Flow<List<Artist>> {
        return detailRepository.getRelatedArtists(artistId, limit)
    }
}
