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
fun TrendingSection(
    tracks: List<Track>,
    onTrackClicked: (Track) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(tracks) { track ->
            TrackSquareTile(
                track = track,
                onClick = { onTrackClicked(track) }
            )
        }
    }
}
