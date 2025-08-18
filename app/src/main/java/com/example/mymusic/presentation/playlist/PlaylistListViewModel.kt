package com.example.mymusic.presentation.playlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.usecase.CreateVirtualPlaylistsUseCase
import com.example.mymusic.domain.usecase.GetTrendingPlaylistsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistListViewModel @Inject constructor(
    private val createVirtualPlaylistsUseCase: CreateVirtualPlaylistsUseCase,
    private val getTrendingPlaylistsUseCase: GetTrendingPlaylistsUseCase
) : ViewModel() {
    
    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists: StateFlow<List<Playlist>> = _playlists.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        Log.d("PlaylistListViewModel", "Initializing PlaylistListViewModel")
        loadPlaylists()
    }
    
    fun loadPlaylists() {
        Log.d("PlaylistListViewModel", "Loading playlists")
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                Log.d("PlaylistListViewModel", "Creating virtual playlists with tracks")
                
                // Create virtual playlists (these will have actual tracks)
                val virtualPlaylists = createVirtualPlaylistsUseCase()
                Log.d("PlaylistListViewModel", "Created ${virtualPlaylists.size} virtual playlists")
                
                if (virtualPlaylists.isNotEmpty()) {
                    val totalTracks = virtualPlaylists.sumOf { it.tracks.size }
                    Log.d("PlaylistListViewModel", "Total tracks across all playlists: $totalTracks")
                    
                    // Log each playlist details
                    virtualPlaylists.forEach { playlist ->
                        Log.d("PlaylistListViewModel", "Playlist: ${playlist.title}, Tracks: ${playlist.tracks.size}")
                    }
                } else {
                    Log.w("PlaylistListViewModel", "No virtual playlists created!")
                }
                
                _playlists.value = virtualPlaylists
            } catch (e: Exception) {
                Log.e("PlaylistListViewModel", "Error creating virtual playlists", e)
                _error.value = "Failed to create playlists: ${e.message}"
            } finally {
                _isLoading.value = false
                Log.d("PlaylistListViewModel", "Virtual playlists creation completed")
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}
