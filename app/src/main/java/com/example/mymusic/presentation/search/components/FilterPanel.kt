package com.example.mymusic.presentation.search.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mymusic.domain.model.SearchFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterPanel(
    currentFilters: SearchFilter,
    onFiltersChanged: (SearchFilter) -> Unit,
    onResetFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    var album by remember { mutableStateOf(currentFilters.album ?: "") }
    var tags by remember { mutableStateOf(currentFilters.tags.joinToString(", ")) }
    var genres by remember { mutableStateOf(currentFilters.genres.joinToString(", ")) }
    var durationMin by remember { mutableStateOf(currentFilters.durationMin?.toString() ?: "") }
    var durationMax by remember { mutableStateOf(currentFilters.durationMax?.toString() ?: "") }
    var yearMin by remember { mutableStateOf(currentFilters.yearMin?.toString() ?: "") }
    var yearMax by remember { mutableStateOf(currentFilters.yearMax?.toString() ?: "") }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Advanced Filters",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            TextButton(onClick = onResetFilters) {
                Text("Reset")
            }
        }
        
        // Album filter
        OutlinedTextField(
            value = album,
            onValueChange = { album = it },
            label = { Text("Album") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        // Tags filter
        OutlinedTextField(
            value = tags,
            onValueChange = { tags = it },
            label = { Text("Tags (comma separated)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        // Genres filter
        OutlinedTextField(
            value = genres,
            onValueChange = { genres = it },
            label = { Text("Genres (comma separated)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        // Duration range
        Text(
            text = "Duration (seconds)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = durationMin,
                onValueChange = { durationMin = it },
                label = { Text("Min") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            
            OutlinedTextField(
                value = durationMax,
                onValueChange = { durationMax = it },
                label = { Text("Max") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }
        
        // Year range
        Text(
            text = "Year",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = yearMin,
                onValueChange = { yearMin = it },
                label = { Text("From") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            
            OutlinedTextField(
                value = yearMax,
                onValueChange = { yearMax = it },
                label = { Text("To") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }
        
        // Apply filters button
        Button(
            onClick = {
                val newFilters = SearchFilter(
                    query = currentFilters.query,
                    album = album.takeIf { it.isNotBlank() },
                    tags = tags.split(",").map { it.trim() }.filter { it.isNotBlank() },
                    genres = genres.split(",").map { it.trim() }.filter { it.isNotBlank() },
                    durationMin = durationMin.toIntOrNull(),
                    durationMax = durationMax.toIntOrNull(),
                    yearMin = yearMin.toIntOrNull(),
                    yearMax = yearMax.toIntOrNull()
                )
                onFiltersChanged(newFilters)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Apply Filters")
        }
    }
}
