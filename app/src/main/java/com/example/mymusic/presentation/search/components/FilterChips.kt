package com.example.mymusic.presentation.search.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mymusic.domain.model.SearchFilter
import com.example.mymusic.domain.model.SearchType
import com.example.mymusic.domain.model.DurationFilter
import com.example.mymusic.domain.model.YearFilter
import com.example.mymusic.domain.model.PopularityFilter
import com.example.mymusic.domain.model.RatingFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChips(
    filter: SearchFilter,
    onFilterRemoved: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!filter.hasActiveFilters()) return
    
    val activeFilters = mutableListOf<String>()
    
    if (filter.searchType != SearchType.TRACK) {
        activeFilters.add("Type: ${filter.searchType.name.lowercase().capitalize()}")
    }
    if (filter.duration != DurationFilter.ANY) {
        activeFilters.add("Duration: ${filter.duration.label}")
    }
    if (filter.year != YearFilter.ANY) {
        activeFilters.add("Year: ${filter.year.label}")
    }
    if (filter.popularity != PopularityFilter.ANY) {
        activeFilters.add("Popularity: ${filter.popularity.label}")
    }
    if (filter.rating != RatingFilter.ANY) {
        activeFilters.add("Rating: ${filter.rating.label}")
    }
    if (filter.genres.isNotEmpty()) {
        activeFilters.add("Genres: ${filter.genres.joinToString(", ")}")
    }
    if (filter.tags.isNotEmpty()) {
        activeFilters.add("Tags: ${filter.tags.joinToString(", ")}")
    }
    
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(activeFilters) { filterText ->
            FilterChip(
                onClick = { },
                label = { Text(filterText) },
                selected = false,
                trailingIcon = {
                    IconButton(
                        onClick = { onFilterRemoved(filterText) },
                        modifier = Modifier.size(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove filter",
                            modifier = Modifier.size(12.dp)
                        )
                    }
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        }
    }
}
