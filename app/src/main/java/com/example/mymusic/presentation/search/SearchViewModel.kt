package com.example.mymusic.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.data.local.SearchHistoryEntity
import com.example.mymusic.domain.model.SearchFilter
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

    private val _currentFilters = MutableStateFlow(SearchFilter.empty())
    val currentFilters: StateFlow<SearchFilter> = _currentFilters.asStateFlow()

    private val _searchHistory = MutableStateFlow<List<SearchHistoryEntity>>(emptyList())
    val searchHistory: StateFlow<List<SearchHistoryEntity>> = _searchHistory.asStateFlow()

    private val _showFilters = MutableStateFlow(false)
    val showFilters: StateFlow<Boolean> = _showFilters.asStateFlow()

    init {
        // Load search history
        loadSearchHistory()
        
        // Debounced search with 300ms delay
        searchQuery
            .debounce(300)
            .filter { it.length >= 2 }
            .onEach { _isLoading.value = true }
            .flatMapLatest { query ->
                val filters = _currentFilters.value
                if (filters.hasFilters()) {
                    searchUseCase.searchWithFilters(filters.copy(query = query))
                } else {
                    searchUseCase(query)
                }
                .catch { e ->
                    _error.value = e.message
                    emit(emptyList())
                }
            }
            .onEach { results ->
                _searchResults.value = results
                _isLoading.value = false
                // Save search to history
                saveSearchToHistory(results.size)
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
    
    // Filter management
    fun updateFilters(filters: SearchFilter) {
        _currentFilters.value = filters.copy(query = _searchQuery.value)
        // Trigger search with new filters
        if (_searchQuery.value.length >= 2) {
            performSearch()
        }
    }
    
    fun resetFilters() {
        _currentFilters.value = SearchFilter.empty()
        // Trigger search without filters
        if (_searchQuery.value.length >= 2) {
            performSearch()
        }
    }
    
    fun toggleFilters() {
        _showFilters.value = !_showFilters.value
    }
    
    fun removeFilter(filterType: String) {
        val current = _currentFilters.value
        val newFilters = when (filterType) {
            "Album" -> current.copy(album = null)
            "Tag" -> current.copy(tags = emptyList())
            "Genre" -> current.copy(genres = emptyList())
            "Min Duration" -> current.copy(durationMin = null)
            "Max Duration" -> current.copy(durationMax = null)
            "From Year" -> current.copy(yearMin = null)
            "To Year" -> current.copy(yearMax = null)
            else -> current
        }
        _currentFilters.value = newFilters
        // Trigger search with updated filters
        if (_searchQuery.value.length >= 2) {
            performSearch()
        }
    }
    
    // Search history management
    fun loadSearchHistory() {
        viewModelScope.launch {
            searchHistoryUseCase.getRecentSearches(100)
                .collect { history ->
                    _searchHistory.value = history
                }
        }
    }
    
    fun onSearchHistorySelected(searchItem: SearchHistoryEntity) {
        _searchQuery.value = searchItem.query
        // TODO: Parse and restore filters from searchItem.filters
    }
    
    fun deleteSearchHistory(searchItem: SearchHistoryEntity) {
        viewModelScope.launch {
            searchHistoryUseCase.deleteSearch(searchItem)
        }
    }
    
    fun clearAllHistory() {
        viewModelScope.launch {
            searchHistoryUseCase.clearAllHistory()
        }
    }
    
    private fun saveSearchToHistory(resultCount: Int) {
        val query = _searchQuery.value
        val filters = _currentFilters.value
        
        if (query.isNotBlank()) {
            viewModelScope.launch {
                searchHistoryUseCase.saveSearch(query, filters, resultCount)
            }
        }
    }
    
    private fun performSearch() {
        // This will trigger the search flow in init block
        // Just update the query to trigger the search
        val currentQuery = _searchQuery.value
        _searchQuery.value = ""
        _searchQuery.value = currentQuery
    }
}
