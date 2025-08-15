package com.example.mymusic.presentation.download

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.data.local.DownloadEntity
import com.example.mymusic.domain.usecase.DeleteDownloadUseCase
import com.example.mymusic.domain.usecase.GetDownloadedTracksUseCase
import com.example.mymusic.domain.usecase.CancelDownloadUseCase
import com.example.mymusic.playback.PlaybackController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import android.util.Log
import javax.inject.Inject

@HiltViewModel
class DownloadsViewModel @Inject constructor(
    private val getDownloadedTracksUseCase: GetDownloadedTracksUseCase,
    private val deleteDownloadUseCase: DeleteDownloadUseCase,
    private val cancelDownloadUseCase: CancelDownloadUseCase,
    private val playbackController: PlaybackController
) : ViewModel() {
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _downloadsState = MutableStateFlow(DownloadsState())
    val downloadsState: StateFlow<DownloadsState> = _downloadsState.asStateFlow()
    
    init {
        loadDownloads()
        observeSearchQuery()
    }
    
    private fun loadDownloads() {
        viewModelScope.launch {
            combine(
                getDownloadedTracksUseCase(),
                _searchQuery
            ) { downloads, query ->
                val filteredDownloads = if (query.isBlank()) {
                    downloads
                } else {
                    downloads.filter { download ->
                        download.title.contains(query, ignoreCase = true) ||
                        download.artist.contains(query, ignoreCase = true)
                    }
                }
                
                DownloadsState(
                    activeDownloads = downloads.filter { 
                        it.status == com.example.mymusic.data.local.DownloadStatus.DOWNLOADING || 
                        it.status == com.example.mymusic.data.local.DownloadStatus.PENDING 
                    },
                    completedDownloads = filteredDownloads.filter { it.status == com.example.mymusic.data.local.DownloadStatus.COMPLETED },
                    searchQuery = query
                )
            }.collect { state ->
                Log.d("DownloadsViewModel", "Downloads state updated: active=${state.activeDownloads.size}, completed=${state.completedDownloads.size}")
                _downloadsState.value = state
            }
        }
    }
    
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery.collect {
                loadDownloads()
            }
        }
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun cancelDownload(trackId: String) {
        viewModelScope.launch {
            try {
                cancelDownloadUseCase(trackId)
                // Downloads will be reloaded automatically through the flow
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    
    fun deleteDownload(trackId: String) {
        viewModelScope.launch {
            try {
                deleteDownloadUseCase(trackId)
                // Downloads will be reloaded automatically through the flow
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    
    fun playDownloadedTrack(download: DownloadEntity) {
        try {
            // Use the local file path for playback
            val localFilePath = download.localFilePath
            if (localFilePath.isNotEmpty()) {
                playbackController.play(
                    trackId = download.trackId,
                    title = download.title,
                    artist = download.artist,
                    artworkUrl = download.imageUrl,
                    url = "file://$localFilePath"
                )
                Log.d("DownloadsViewModel", "Playing downloaded track: ${download.title} from $localFilePath")
            } else {
                Log.e("DownloadsViewModel", "No local file path for track: ${download.title}")
            }
        } catch (e: Exception) {
            Log.e("DownloadsViewModel", "Error playing downloaded track: ${download.title}", e)
        }
    }
    
    fun shareDownload(download: DownloadEntity) {
        // TODO: Implement share functionality
        // This would typically open a share intent
        println("Sharing download: ${download.title}")
    }
    
    fun openSettings() {
        // TODO: Implement settings navigation
        // This would typically navigate to settings screen
        println("Opening download settings")
    }
    
    // Debug method to check downloads
    fun debugDownloads() {
        viewModelScope.launch {
            try {
                val downloads = getDownloadedTracksUseCase().first()
                Log.d("DownloadsViewModel", "Debug: Found ${downloads.size} downloads in database")
                downloads.forEach { download ->
                    Log.d("DownloadsViewModel", "Debug: Download - ${download.title} (${download.status}) - Progress: ${download.progress}% - DownloadId: ${download.downloadId}")
                }
            } catch (e: Exception) {
                Log.e("DownloadsViewModel", "Debug: Error getting downloads", e)
            }
        }
    }
}

data class DownloadsState(
    val activeDownloads: List<DownloadEntity> = emptyList(),
    val completedDownloads: List<DownloadEntity> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
