package com.example.mymusic.presentation.discovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.domain.model.Track
import com.example.mymusic.domain.usecase.DiscoveryUseCase
import com.example.mymusic.playback.PlaybackController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoveryViewModel @Inject constructor(
    private val discoveryUseCase: DiscoveryUseCase,
    private val playbackController: PlaybackController
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoveryUiState())
    val uiState: StateFlow<DiscoveryUiState> = _uiState.asStateFlow()

    init {
        loadDiscoveryContent()
    }

    fun refresh() {
        loadDiscoveryContent()
    }

    fun loadTracksByGenre(genre: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingGenre = true) }
            
            discoveryUseCase.getTracksByGenre(genre, 20, 0)
                .catch { e ->
                    _uiState.update { it.copy(
                        isLoadingGenre = false,
                        error = e.message
                    ) }
                }
                .collect { tracks ->
                    _uiState.update { it.copy(
                        genreTracks = tracks,
                        isLoadingGenre = false
                    ) }
                }
        }
    }

    fun onTrackClicked(track: Track) {
        val url = track.audioUrl ?: return
        
        playbackController.play(
            trackId = track.id,
            title = track.title,
            artist = track.artist,
            artworkUrl = track.artworkUrl,
            url = url
        )
    }

    private fun loadDiscoveryContent() {
        // Load featured content
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingFeatured = true) }
            
            discoveryUseCase.getFeaturedContent(10)
                .catch { e ->
                    _uiState.update { it.copy(
                        isLoadingFeatured = false,
                        error = e.message
                    ) }
                }
                .collect { tracks ->
                    _uiState.update { it.copy(
                        featuredContent = tracks,
                        isLoadingFeatured = false
                    ) }
                }
        }

        // Load trending tracks
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingTrending = true) }
            
            discoveryUseCase.getTrendingTracks(20, 0)
                .catch { e ->
                    _uiState.update { it.copy(
                        isLoadingTrending = false,
                        error = e.message
                    ) }
                }
                .collect { tracks ->
                    _uiState.update { it.copy(
                        trendingTracks = tracks,
                        isLoadingTrending = false
                    ) }
                }
        }

        // Load new releases
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingNewReleases = true) }
            
            discoveryUseCase.getNewReleases(20, 0)
                .catch { e ->
                    _uiState.update { it.copy(
                        isLoadingNewReleases = false,
                        error = e.message
                    ) }
                }
                .collect { tracks ->
                    _uiState.update { it.copy(
                        newReleases = tracks,
                        isLoadingNewReleases = false
                    ) }
                }
        }
    }
}

data class DiscoveryUiState(
    val featuredContent: List<Track> = emptyList(),
    val trendingTracks: List<Track> = emptyList(),
    val newReleases: List<Track> = emptyList(),
    val genreTracks: List<Track> = emptyList(),
    val isLoadingFeatured: Boolean = false,
    val isLoadingTrending: Boolean = false,
    val isLoadingNewReleases: Boolean = false,
    val isLoadingGenre: Boolean = false,
    val error: String? = null
)
