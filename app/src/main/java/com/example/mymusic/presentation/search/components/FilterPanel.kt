package com.example.mymusic.presentation.search.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mymusic.domain.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterPanel(
    currentFilter: SearchFilter,
    onFilterChanged: (SearchFilter) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var localFilter by remember { mutableStateOf(currentFilter) }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filters",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Search Filters",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close filters"
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Filter content
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {
            // Search Type
            Text(
                text = "Search Type",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            SearchTypeSelector(
                selected = localFilter.searchType,
                onSelectionChanged = { 
                    localFilter = localFilter.copy(searchType = it)
                }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Duration Filter
            Text(
                text = "Duration",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            DurationFilterSelector(
                selected = localFilter.duration,
                onSelectionChanged = { 
                    localFilter = localFilter.copy(duration = it)
                }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Year Filter
            Text(
                text = "Year",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            YearFilterSelector(
                selected = localFilter.year,
                onSelectionChanged = { 
                    localFilter = localFilter.copy(year = it)
                }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Popularity Filter
            Text(
                text = "Popularity",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            PopularityFilterSelector(
                selected = localFilter.popularity,
                onSelectionChanged = { 
                    localFilter = localFilter.copy(popularity = it)
                }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Rating Filter
            Text(
                text = "Rating",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            RatingFilterSelector(
                selected = localFilter.rating,
                onSelectionChanged = { 
                    localFilter = localFilter.copy(rating = it)
                }
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = {
                    localFilter = localFilter.clearFilters()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Clear All")
            }
            
            Button(
                onClick = {
                    onFilterChanged(localFilter)
                    onClose()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Apply Filters")
            }
        }
    }
}

@Composable
private fun SearchTypeSelector(
    selected: SearchType,
    onSelectionChanged: (SearchType) -> Unit
) {
    Column {
        SearchType.values().forEach { searchType ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selected == searchType,
                    onClick = { onSelectionChanged(searchType) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = searchType.name.lowercase().capitalize(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun DurationFilterSelector(
    selected: DurationFilter,
    onSelectionChanged: (DurationFilter) -> Unit
) {
    Column {
        DurationFilter.values().forEach { duration ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selected == duration,
                    onClick = { onSelectionChanged(duration) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = duration.label,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun YearFilterSelector(
    selected: YearFilter,
    onSelectionChanged: (YearFilter) -> Unit
) {
    Column {
        YearFilter.values().forEach { year ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selected == year,
                    onClick = { onSelectionChanged(year) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = year.label,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun PopularityFilterSelector(
    selected: PopularityFilter,
    onSelectionChanged: (PopularityFilter) -> Unit
) {
    Column {
        PopularityFilter.values().forEach { popularity ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selected == popularity,
                    onClick = { onSelectionChanged(popularity) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = popularity.label,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun RatingFilterSelector(
    selected: RatingFilter,
    onSelectionChanged: (RatingFilter) -> Unit
) {
    Column {
        RatingFilter.values().forEach { rating ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selected == rating,
                    onClick = { onSelectionChanged(rating) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = rating.label,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
