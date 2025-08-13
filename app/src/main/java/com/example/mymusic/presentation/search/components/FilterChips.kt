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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChips(
    filters: SearchFilter,
    onFilterRemoved: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeFilters = mutableListOf<Pair<String, String>>()
    
    // Collect active filters
    filters.album?.let { activeFilters.add("Album" to it) }
    filters.tags.forEach { tag -> activeFilters.add("Tag" to tag) }
    filters.genres.forEach { genre -> activeFilters.add("Genre" to genre) }
    filters.durationMin?.let { activeFilters.add("Min Duration" to "${it}s") }
    filters.durationMax?.let { activeFilters.add("Max Duration" to "${it}s") }
    filters.yearMin?.let { activeFilters.add("From Year" to it.toString()) }
    filters.yearMax?.let { activeFilters.add("To Year" to it.toString()) }
    
    if (activeFilters.isNotEmpty()) {
        LazyRow(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(activeFilters) { (filterType, value) ->
                FilterChip(
                    selected = false,
                    onClick = { onFilterRemoved(filterType) },
                    label = { Text("$filterType: $value") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove filter"
                        )
                    }
                )
            }
        }
    }
}
