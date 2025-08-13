package com.example.mymusic.presentation.discovery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.domain.model.Track
import com.example.mymusic.domain.usecase.DiscoveryUseCase
import com.example.mymusic.playback.PlaybackController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoveryViewModel @Inject constructor(
    private val discoveryUseCase: DiscoveryUseCase,
    private val playbackController: PlaybackController
) : ViewModel() {
    
    private val _trendingTracks = MutableStateFlow<List<Track>>(emptyList())
    val trendingTracks: StateFlow<List<Track>> = _trendingTracks.asStateFlow()
    
    private val _newReleases = MutableStateFlow<List<Track>>(emptyList())
    val newReleases: StateFlow<List<Track>> = _newReleases.asStateFlow()
    
    private val _featuredTracks = MutableStateFlow<List<Track>>(emptyList())
    val featuredTracks: StateFlow<List<Track>> = _featuredTracks.asStateFlow()
    
    private val _selectedGenre = MutableStateFlow<String?>(null)
    val selectedGenre: StateFlow<String?> = _selectedGenre.asStateFlow()
    
    private val _genreTracks = MutableStateFlow<List<Track>>(emptyList())
    val genreTracks: StateFlow<List<Track>> = _genreTracks.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        Log.d("DiscoveryViewModel", "Initializing DiscoveryViewModel")
        loadDiscoveryContent()
    }
    
    fun loadDiscoveryContent() {
        Log.d("DiscoveryViewModel", "Loading discovery content")
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            // Load trending tracks
            launch {
                try {
                    Log.d("DiscoveryViewModel", "Loading trending tracks")
                    val tracks = discoveryUseCase.getTrendingTracks(limit = 10).first()
                    Log.d("DiscoveryViewModel", "Received ${tracks.size} trending tracks")
                    _trendingTracks.value = tracks
                } catch (e: Exception) {
                    Log.e("DiscoveryViewModel", "Error loading trending tracks", e)
                    _error.value = "Failed to load trending tracks: ${e.message}"
                }
            }
            
            // Load new releases
            launch {
                try {
                    Log.d("DiscoveryViewModel", "Loading new releases")
                    val tracks = discoveryUseCase.getNewReleases(limit = 10).first()
                    Log.d("DiscoveryViewModel", "Received ${tracks.size} new releases")
                    _newReleases.value = tracks
                } catch (e: Exception) {
                    Log.e("DiscoveryViewModel", "Error loading new releases", e)
                    _error.value = "Failed to load new releases: ${e.message}"
                }
            }
            
            // Load featured tracks
            launch {
                try {
                    Log.d("DiscoveryViewModel", "Loading featured tracks")
                    val tracks = discoveryUseCase.getFeaturedTracks(limit = 5).first()
                    Log.d("DiscoveryViewModel", "Received ${tracks.size} featured tracks")
                    _featuredTracks.value = tracks
                } catch (e: Exception) {
                    Log.e("DiscoveryViewModel", "Error loading featured tracks", e)
                    _error.value = "Failed to load featured tracks: ${e.message}"
                }
            }
            
            _isLoading.value = false
            Log.d("DiscoveryViewModel", "Discovery content loading completed")
        }
    }
    
    fun selectGenre(genre: String) {
        Log.d("DiscoveryViewModel", "Selecting genre: $genre")
        _selectedGenre.value = genre
        loadTracksByGenre(genre)
    }
    
    private fun loadTracksByGenre(genre: String) {
        viewModelScope.launch {
            try {
                Log.d("DiscoveryViewModel", "Loading tracks for genre: $genre")
                val tracks = discoveryUseCase.getTracksByGenre(genre, limit = 20).first()
                Log.d("DiscoveryViewModel", "Received ${tracks.size} tracks for genre: $genre")
                _genreTracks.value = tracks
            } catch (e: Exception) {
                Log.e("DiscoveryViewModel", "Error loading tracks for genre: $genre", e)
                _error.value = "Failed to load tracks for genre: $genre"
            }
        }
    }
    
    fun onTrackClicked(track: Track) {
        Log.d("DiscoveryViewModel", "Track clicked: ${track.title}")
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
    
    fun refresh() {
        Log.d("DiscoveryViewModel", "Refreshing discovery content")
        loadDiscoveryContent()
    }
}
