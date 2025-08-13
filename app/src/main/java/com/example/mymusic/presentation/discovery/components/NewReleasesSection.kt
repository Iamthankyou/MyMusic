package com.example.mymusic.presentation.discovery.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mymusic.domain.model.Track
import com.example.mymusic.presentation.components.TrackItem

@Composable
fun NewReleasesSection(
    tracks: List<Track>,
    onTrackClicked: (Track) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(tracks) { track ->
            Card(
                modifier = Modifier.width(160.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    TrackItem(
                        track = track,
                        onClick = { onTrackClicked(track) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
