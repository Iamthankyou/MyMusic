package com.example.mymusic.data.repository

import android.util.Log
import com.example.mymusic.data.mapper.AlbumMapper
import com.example.mymusic.data.mapper.ArtistMapper
import com.example.mymusic.data.mapper.TrackMapper
import com.example.mymusic.data.remote.JamendoTracksService
import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.model.Artist
import com.example.mymusic.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailRepository @Inject constructor(
    private val service: JamendoTracksService,
    private val trackMapper: TrackMapper,
    private val artistMapper: ArtistMapper,
    private val albumMapper: AlbumMapper
) {
    
    fun getTrackDetail(trackId: String): Flow<Track?> = flow {
        Log.d("DetailRepository", "Fetching track detail: $trackId")
        val response = service.getTrackDetail(trackId)
        Log.d("DetailRepository", "API Response: headers=${response.headers}, results count=${response.results.size}")
        
        if (response.results.isEmpty()) {
            Log.w("DetailRepository", "No results found for track ID: $trackId")
            emit(null)
            return@flow
        }
        
        val trackDto = response.results.first()
        Log.d("DetailRepository", "Track DTO: id=${trackDto.id}, name=${trackDto.name}, artist=${trackDto.artist_name}")
        
        val track = trackMapper.fromDto(trackDto)
        Log.d("DetailRepository", "Mapped Track: id=${track.id}, title=${track.title}, artist=${track.artist}")
        emit(track)
    }.catch { e ->
        Log.e("DetailRepository", "Error fetching track detail: $trackId", e)
        emit(null)
    }
    
    fun getArtistDetail(artistId: String): Flow<Artist?> = flow {
        Log.d("DetailRepository", "Fetching artist detail: $artistId")
        val response = service.getArtistDetail(artistId)
        val artist = response.results.firstOrNull()?.let { artistMapper.fromDto(it) }
        emit(artist)
    }.catch { e ->
        Log.e("DetailRepository", "Error fetching artist detail: $artistId", e)
        emit(null)
    }
    
    fun getArtistTracks(artistId: String, limit: Int = 20, offset: Int = 0): Flow<List<Track>> = flow {
        Log.d("DetailRepository", "Fetching artist tracks: $artistId, limit=$limit, offset=$offset")
        val response = service.getArtistTracks(artistId, limit, offset)
        val tracks = response.results.map { trackMapper.fromDto(it) }
        emit(tracks)
    }.catch { e ->
        Log.e("DetailRepository", "Error fetching artist tracks: $artistId", e)
        emit(emptyList())
    }
    
    fun getAlbumDetail(albumId: String): Flow<Album?> = flow {
        Log.d("DetailRepository", "Fetching album detail: $albumId")
        val response = service.getAlbumDetail(albumId)
        val album = response.results.firstOrNull()?.let { albumMapper.fromDto(it) }
        emit(album)
    }.catch { e ->
        Log.e("DetailRepository", "Error fetching album detail: $albumId", e)
        emit(null)
    }
    
    fun getRelatedTracks(trackId: String, limit: Int = 10): Flow<List<Track>> = flow {
        Log.d("DetailRepository", "Fetching related tracks: $trackId, limit=$limit")
        val response = service.getRelatedTracks(trackId, limit)
        val tracks = response.results.map { trackMapper.fromDto(it) }
        emit(tracks)
    }.catch { e ->
        Log.e("DetailRepository", "Error fetching related tracks: $trackId", e)
        emit(emptyList())
    }
    
    fun getRelatedArtists(artistId: String, limit: Int = 10): Flow<List<Artist>> = flow {
        Log.d("DetailRepository", "Fetching related artists: $artistId, limit=$limit")
        val response = service.getRelatedArtists(artistId, limit)
        val artists = response.results.map { artistMapper.fromDto(it) }
        emit(artists)
    }.catch { e ->
        Log.e("DetailRepository", "Error fetching related artists: $artistId", e)
        emit(emptyList())
    }
}
