package com.example.mymusic.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.domain.model.Track
import com.example.mymusic.domain.usecase.SearchUseCase
import com.example.mymusic.playback.PlaybackController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val playbackController: PlaybackController,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val trackId: String = checkNotNull(savedStateHandle["trackId"])
    private val trackTitle: String = checkNotNull(savedStateHandle["trackTitle"])
    private val trackArtist: String = checkNotNull(savedStateHandle["trackArtist"])
    private val trackArtworkUrl: String? = savedStateHandle["trackArtworkUrl"]
    private val trackAudioUrl: String? = savedStateHandle["trackAudioUrl"]
    private val trackDurationMs: Long = savedStateHandle["trackDurationMs"] ?: 0L

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadTrackDetails()
        loadRelatedTracks()
    }

    fun onPlayClicked() {
        val audioUrl = trackAudioUrl ?: return
        
        val track = Track(
            id = trackId,
            title = trackTitle,
            artist = trackArtist,
            durationMs = trackDurationMs,
            artworkUrl = trackArtworkUrl,
            audioUrl = audioUrl,
            isDownloadable = false
        )
        
        playbackController.play(
            trackId = track.id,
            title = track.title,
            artist = track.artist,
            artworkUrl = track.artworkUrl,
            url = track.audioUrl!!
        )
    }

    fun onShareClicked() {
        // TODO: Implement share functionality
        // This would typically use Android's Share Intent
    }

    fun onRelatedTrackClicked(track: Track) {
        val url = track.audioUrl ?: return
        
        playbackController.play(
            trackId = track.id,
            title = track.title,
            artist = track.artist,
            artworkUrl = track.artworkUrl,
            url = url
        )
    }

    private fun loadTrackDetails() {
        val track = Track(
            id = trackId,
            title = trackTitle,
            artist = trackArtist,
            durationMs = trackDurationMs,
            artworkUrl = trackArtworkUrl,
            audioUrl = trackAudioUrl,
            isDownloadable = false
        )
        
        _uiState.update { it.copy(track = track) }
    }

    private fun loadRelatedTracks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingRelated = true) }
            
            try {
                // Search for tracks by the same artist
                searchUseCase("artist:$trackArtist", 10, 0)
                    .catch { e ->
                        _uiState.update { it.copy(
                            isLoadingRelated = false,
                            error = e.message
                        ) }
                    }
                    .collect { tracks ->
                        val relatedTracks = tracks.filter { it.id != trackId }
                        _uiState.update { it.copy(
                            relatedTracks = relatedTracks,
                            isLoadingRelated = false
                        ) }
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoadingRelated = false,
                    error = e.message
                ) }
            }
        }
    }
}

data class DetailUiState(
    val track: Track? = null,
    val relatedTracks: List<Track> = emptyList(),
    val isLoadingRelated: Boolean = false,
    val error: String? = null
)
