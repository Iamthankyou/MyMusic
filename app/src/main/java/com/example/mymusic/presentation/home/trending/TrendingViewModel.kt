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
import com.example.mymusic.data.paging.TrendingTracksPagingSource
import com.example.mymusic.data.remote.JamendoTracksService
import com.example.mymusic.domain.model.Track
import com.example.mymusic.playback.PlaybackController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendingViewModel @Inject constructor(
    private val service: JamendoTracksService,
    private val controller: PlaybackController
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
        
        viewModelScope.launch {
            if (currentIndex != -1) {
                // Track found in loaded tracks, set queue with all tracks
                controller.setQueue(allTracks)
                // Set current index to the clicked track
                controller.playAt(currentIndex)
            } else {
                // Track not found, just play this single track
                controller.setQueue(listOf(track))
            }
        }
        
        controller.play(
            trackId = track.id,
            title = track.title,
            artist = track.artist,
            artworkUrl = track.artworkUrl,
            url = url
        )
    }
    
    fun updateLoadedTracks(tracks: List<Track>) {
        _loadedTracks.value = tracks
    }
}


