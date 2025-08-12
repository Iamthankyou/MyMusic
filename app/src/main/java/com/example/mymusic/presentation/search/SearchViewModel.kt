package com.example.mymusic.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.domain.model.Track
import com.example.mymusic.domain.usecase.SearchUseCase
import com.example.mymusic.playback.PlaybackController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(FlowPreview::class)
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val playbackController: PlaybackController
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Track>>(emptyList())
    val searchResults: StateFlow<List<Track>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        // Debounced search with 300ms delay
        searchQuery
            .debounce(300)
            .filter { it.length >= 2 }
            .onEach { _isLoading.value = true }
            .flatMapLatest { query ->
                searchUseCase(query)
                    .catch { e ->
                        _error.value = e.message
                        emit(emptyList())
                    }
            }
            .onEach { results ->
                _searchResults.value = results
                _isLoading.value = false
            }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        if (query.length < 2) {
            _searchResults.value = emptyList()
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

    fun clearError() {
        _error.value = null
    }
}
