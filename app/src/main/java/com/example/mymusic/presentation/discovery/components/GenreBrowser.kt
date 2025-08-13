package com.example.mymusic.presentation.discovery.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenreBrowser(
    selectedGenre: String?,
    onGenreSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val genres = listOf(
        "Rock", "Pop", "Jazz", "Classical", "Electronic", 
        "Hip Hop", "Country", "Blues", "Folk", "R&B"
    )
    
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(genres) { genre ->
            FilterChip(
                selected = selectedGenre == genre,
                onClick = { onGenreSelected(genre) },
                label = { Text(genre) }
            )
        }
    }
}
