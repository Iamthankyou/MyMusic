package com.example.mymusic.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.data.local.SearchHistoryEntity
import com.example.mymusic.domain.model.*
import com.example.mymusic.domain.model.Track
import com.example.mymusic.domain.usecase.SearchHistoryUseCase
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
    private val searchHistoryUseCase: SearchHistoryUseCase,
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

    private val _currentFilter = MutableStateFlow(SearchFilter())
    val currentFilter: StateFlow<SearchFilter> = _currentFilter.asStateFlow()

    private val _searchHistory = MutableStateFlow<List<SearchHistoryEntity>>(emptyList())
    val searchHistory: StateFlow<List<SearchHistoryEntity>> = _searchHistory.asStateFlow()

    private val _showFilterPanel = MutableStateFlow(false)
    val showFilterPanel: StateFlow<Boolean> = _showFilterPanel.asStateFlow()

    init {
        // Load search history
        viewModelScope.launch {
            searchHistoryUseCase.getRecentSearches(10).collect { history ->
                _searchHistory.value = history
            }
        }
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

    fun onFilterChanged(filter: SearchFilter) {
        _currentFilter.value = filter
        // Trigger search with new filter
        if (_searchQuery.value.isNotEmpty()) {
            performSearch(_searchQuery.value, filter)
        }
    }

    fun onFilterRemoved(filterText: String) {
        val currentFilter = _currentFilter.value
        val newFilter = when {
            filterText.startsWith("Type:") -> currentFilter.copy(searchType = SearchType.TRACK)
            filterText.startsWith("Duration:") -> currentFilter.copy(duration = DurationFilter.ANY)
            filterText.startsWith("Year:") -> currentFilter.copy(year = YearFilter.ANY)
            filterText.startsWith("Popularity:") -> currentFilter.copy(popularity = PopularityFilter.ANY)
            filterText.startsWith("Rating:") -> currentFilter.copy(rating = RatingFilter.ANY)
            filterText.startsWith("Genres:") -> currentFilter.copy(genres = emptyList())
            filterText.startsWith("Tags:") -> currentFilter.copy(tags = emptyList())
            else -> currentFilter
        }
        onFilterChanged(newFilter)
    }

    fun toggleFilterPanel() {
        _showFilterPanel.value = !_showFilterPanel.value
    }

    fun onSearchHistorySelected(query: String) {
        _searchQuery.value = query
        performSearch(query, _currentFilter.value)
    }

    fun onSearchHistoryDeleted(search: SearchHistoryEntity) {
        viewModelScope.launch {
            searchHistoryUseCase.deleteSearch(search)
        }
    }

    private fun performSearch(query: String, filter: SearchFilter) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Save search to history
                searchHistoryUseCase.saveSearch(query, filter)
                
                // Perform search with filter
                val results = if (filter.hasActiveFilters()) {
                    searchUseCase.searchWithFilters(filter.copy(query = query))
                } else {
                    searchUseCase(query)
                }
                
                results.collect { tracks ->
                    _searchResults.value = tracks
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }
}
