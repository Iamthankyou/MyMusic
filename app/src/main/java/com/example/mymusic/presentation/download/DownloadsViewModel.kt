package com.example.mymusic.presentation.download

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.data.local.DownloadEntity
import com.example.mymusic.domain.usecase.DeleteDownloadUseCase
import com.example.mymusic.domain.usecase.GetDownloadedTracksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadsViewModel @Inject constructor(
    private val getDownloadedTracksUseCase: GetDownloadedTracksUseCase,
    private val deleteDownloadUseCase: DeleteDownloadUseCase
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
                    activeDownloads = downloads.filter { it.status == com.example.mymusic.data.local.DownloadStatus.DOWNLOADING },
                    completedDownloads = filteredDownloads.filter { it.status == com.example.mymusic.data.local.DownloadStatus.COMPLETED },
                    searchQuery = query
                )
            }.collect { state ->
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
        // Implementation for canceling download
        // This would typically call a use case to cancel the download
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
    
    fun shareDownload(download: DownloadEntity) {
        // Implementation for sharing download
        // This would typically open a share intent
    }
    
    fun openSettings() {
        // Implementation for opening download settings
        // This would typically navigate to settings screen
    }
}

data class DownloadsState(
    val activeDownloads: List<DownloadEntity> = emptyList(),
    val completedDownloads: List<DownloadEntity> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
