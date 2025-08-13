package com.example.mymusic.presentation.search.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mymusic.data.local.SearchHistoryEntity
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchHistory(
    searchHistory: List<SearchHistoryEntity>,
    onSearchSelected: (SearchHistoryEntity) -> Unit,
    onSearchDeleted: (SearchHistoryEntity) -> Unit,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Search History"
                )
                Text(
                    text = "Search History",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            
            if (searchHistory.isNotEmpty()) {
                TextButton(onClick = onClearHistory) {
                    Text("Clear All")
                }
            }
        }
        
        if (searchHistory.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No search history yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(searchHistory) { searchItem ->
                    SearchHistoryItem(
                        searchItem = searchItem,
                        onSearchSelected = onSearchSelected,
                        onSearchDeleted = onSearchDeleted
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchHistoryItem(
    searchItem: SearchHistoryEntity,
    onSearchSelected: (SearchHistoryEntity) -> Unit,
    onSearchDeleted: (SearchHistoryEntity) -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm")
    
    Card(
        onClick = { onSearchSelected(searchItem) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = searchItem.query,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = searchItem.timestamp.format(formatter),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                if (searchItem.filters != null) {
                    Text(
                        text = "Filters: ${searchItem.filters}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                if (searchItem.resultCount > 0) {
                    Text(
                        text = "${searchItem.resultCount} results",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            IconButton(onClick = { onSearchDeleted(searchItem) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete search history"
                )
            }
        }
    }
}
