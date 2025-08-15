package com.example.mymusic.presentation.home.trending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import com.example.mymusic.data.paging.TrendingTracksPagingSource
import com.example.mymusic.data.remote.JamendoTracksService
import com.example.mymusic.domain.model.Track
import com.example.mymusic.playback.PlaybackController
import com.example.mymusic.domain.usecase.DownloadTrackUseCase
import com.example.mymusic.domain.repository.DownloadRepository
import com.example.mymusic.data.local.DownloadStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendingViewModel @Inject constructor(
    private val service: JamendoTracksService,
    private val controller: PlaybackController,
    private val downloadTrackUseCase: DownloadTrackUseCase,
    private val downloadRepository: DownloadRepository
) : ViewModel() {

    private val _loadedTracks = MutableStateFlow<List<Track>>(emptyList())
    val loadedTracks: StateFlow<List<Track>> = _loadedTracks

    val pagingData: Flow<PagingData<Track>> = Pager(
        config = PagingConfig(pageSize = 20, prefetchDistance = 2),
        pagingSourceFactory = { TrendingTracksPagingSource(service, 20) }
    ).flow.cachedIn(viewModelScope)

    fun onTrackClicked(track: Track) {
        val url = track.audioUrl ?: return
        
        // Get all loaded tracks and find the current track index
        val allTracks = _loadedTracks.value
        val currentIndex = allTracks.indexOfFirst { it.id == track.id }
        
        if (currentIndex != -1) {
            // Track found in loaded tracks, set queue and play at index
            viewModelScope.launch {
                controller.setQueue(allTracks)
                controller.setCurrentIndex(currentIndex)
                kotlinx.coroutines.delay(100) // Small delay to ensure queue is set
                controller.playAt(currentIndex)
            }
        } else {
            // Track not found, just play this single track
            controller.play(
                trackId = track.id,
                title = track.title,
                artist = track.artist,
                artworkUrl = track.artworkUrl,
                url = url
            )
            
            // Set this track as queue for next/previous
            viewModelScope.launch {
                kotlinx.coroutines.delay(100)
                controller.setQueue(listOf(track))
                controller.setCurrentIndex(0)
            }
        }
    }
    
    fun updateLoadedTracks(tracks: List<Track>) {
        _loadedTracks.value = tracks
    }
    
    fun onDownloadClicked(track: Track) {
        viewModelScope.launch {
            try {
                downloadTrackUseCase(track)
                // TODO: Show success message or update UI
            } catch (e: Exception) {
                // TODO: Show error message
                println("Download failed: ${e.message}")
            }
        }
    }
    
    // Get download status as Flow for reactive UI updates
    fun getDownloadStatusFlow(trackId: String): Flow<DownloadStatus?> {
        return downloadRepository.getDownloadStatus(trackId)
    }
    
    // Get download status synchronously (for immediate UI updates)
    suspend fun getDownloadStatus(trackId: String): DownloadStatus? {
        return try {
            downloadRepository.getDownloadStatus(trackId).first()
        } catch (e: Exception) {
            null
        }
    }
}


