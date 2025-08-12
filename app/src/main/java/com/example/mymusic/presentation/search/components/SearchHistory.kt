package com.example.mymusic.presentation.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mymusic.data.local.SearchHistoryEntity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SearchHistory(
    searches: List<SearchHistoryEntity>,
    onSearchSelected: (String) -> Unit,
    onSearchDeleted: (SearchHistoryEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    if (searches.isEmpty()) return
    
    Column(modifier = modifier) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = "Search history",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Recent Searches",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        }
        
        // Search history list
        LazyColumn {
            items(searches) { search ->
                SearchHistoryItem(
                    search = search,
                    onSearchSelected = onSearchSelected,
                    onSearchDeleted = onSearchDeleted
                )
            }
        }
    }
}

@Composable
private fun SearchHistoryItem(
    search: SearchHistoryEntity,
    onSearchSelected: (String) -> Unit,
    onSearchDeleted: (SearchHistoryEntity) -> Unit
) {
    val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(search.timestamp))
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSearchSelected(search.query) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = search.query,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        IconButton(
            onClick = { onSearchDeleted(search) },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete search",
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
