package com.example.mymusic.presentation.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.domain.model.Album
import com.example.mymusic.domain.model.Artist
import com.example.mymusic.domain.model.Track
import com.example.mymusic.domain.usecase.DetailUseCase
import com.example.mymusic.playback.PlaybackController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val detailUseCase: DetailUseCase,
    private val playbackController: PlaybackController
) : ViewModel() {
    
    private val _trackDetail = MutableStateFlow<Track?>(null)
    val trackDetail: StateFlow<Track?> = _trackDetail.asStateFlow()
    
    private val _artistDetail = MutableStateFlow<Artist?>(null)
    val artistDetail: StateFlow<Artist?> = _artistDetail.asStateFlow()
    
    private val _albumDetail = MutableStateFlow<Album?>(null)
    val albumDetail: StateFlow<Album?> = _albumDetail.asStateFlow()
    
    private val _artistTracks = MutableStateFlow<List<Track>>(emptyList())
    val artistTracks: StateFlow<List<Track>> = _artistTracks.asStateFlow()
    
    private val _relatedTracks = MutableStateFlow<List<Track>>(emptyList())
    val relatedTracks: StateFlow<List<Track>> = _relatedTracks.asStateFlow()
    
    private val _relatedArtists = MutableStateFlow<List<Artist>>(emptyList())
    val relatedArtists: StateFlow<List<Artist>> = _relatedArtists.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    fun loadTrackDetail(trackId: String) {
        Log.d("DetailViewModel", "Loading track detail: $trackId")
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val track = detailUseCase.getTrackDetail(trackId).first()
                _trackDetail.value = track
                
                if (track != null) {
                    loadRelatedTracks(trackId)
                }
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error loading track detail: $trackId", e)
                _error.value = "Failed to load track details: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun loadArtistDetail(artistId: String) {
        Log.d("DetailViewModel", "Loading artist detail: $artistId")
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val artist = detailUseCase.getArtistDetail(artistId).first()
                _artistDetail.value = artist
                
                if (artist != null) {
                    loadArtistTracks(artistId)
                    loadRelatedArtists(artistId)
                }
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error loading artist detail: $artistId", e)
                _error.value = "Failed to load artist details: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun loadAlbumDetail(albumId: String) {
        Log.d("DetailViewModel", "Loading album detail: $albumId")
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val album = detailUseCase.getAlbumDetail(albumId).first()
                _albumDetail.value = album
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error loading album detail: $albumId", e)
                _error.value = "Failed to load album details: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun loadArtistTracks(artistId: String) {
        viewModelScope.launch {
            try {
                val tracks = detailUseCase.getArtistTracks(artistId, limit = 20).first()
                _artistTracks.value = tracks
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error loading artist tracks: $artistId", e)
            }
        }
    }
    
    private fun loadRelatedTracks(trackId: String) {
        viewModelScope.launch {
            try {
                val tracks = detailUseCase.getRelatedTracks(trackId, limit = 10).first()
                _relatedTracks.value = tracks
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error loading related tracks: $trackId", e)
            }
        }
    }
    
    private fun loadRelatedArtists(artistId: String) {
        viewModelScope.launch {
            try {
                val artists = detailUseCase.getRelatedArtists(artistId, limit = 10).first()
                _relatedArtists.value = artists
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error loading related artists: $artistId", e)
            }
        }
    }
    
    fun onTrackClicked(track: Track) {
        Log.d("DetailViewModel", "Track clicked: ${track.title}")
        playbackController.play(
            trackId = track.id,
            title = track.title,
            artist = track.artist,
            artworkUrl = track.artworkUrl ?: "",
            url = track.audioUrl ?: ""
        )
    }
    
    fun clearError() {
        _error.value = null
    }
}
