package com.example.mymusic.presentation.playlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.usecase.GetPlaylistDetailUseCase
import com.example.mymusic.domain.usecase.CreateVirtualPlaylistsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailViewModel @Inject constructor(
    private val getPlaylistDetailUseCase: GetPlaylistDetailUseCase,
    private val createVirtualPlaylistsUseCase: CreateVirtualPlaylistsUseCase
) : ViewModel() {
    
    private val _playlistDetail = MutableStateFlow<Playlist?>(null)
    val playlistDetail: StateFlow<Playlist?> = _playlistDetail.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    fun loadPlaylistDetail(playlistId: String) {
        Log.d("PlaylistDetailViewModel", "Loading playlist detail: $playlistId")
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                // Check if it's a virtual playlist
                if (playlistId.startsWith("jamendo_virtual_")) {
                    Log.d("PlaylistDetailViewModel", "Loading virtual playlist")
                    val virtualPlaylists = createVirtualPlaylistsUseCase()
                    val virtualPlaylist = virtualPlaylists.find { it.id == playlistId }
                    
                    if (virtualPlaylist != null) {
                        Log.d("PlaylistDetailViewModel", "Found virtual playlist: ${virtualPlaylist.title}")
                        _playlistDetail.value = virtualPlaylist
                    } else {
                        _error.value = "Virtual playlist not found"
                    }
                } else {
                    // Load from Jamendo API
                    Log.d("PlaylistDetailViewModel", "Loading Jamendo playlist detail")
                    val playlist = getPlaylistDetailUseCase(playlistId)
                    Log.d("PlaylistDetailViewModel", "Received Jamendo playlist: ${playlist?.title}")
                    _playlistDetail.value = playlist
                    
                    if (playlist == null) {
                        _error.value = "Jamendo playlist not found"
                    }
                }
            } catch (e: Exception) {
                Log.e("PlaylistDetailViewModel", "Error loading playlist detail", e)
                _error.value = "Failed to load playlist: ${e.message}"
            } finally {
                _isLoading.value = false
                Log.d("PlaylistDetailViewModel", "Playlist detail loading completed")
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}
