package com.example.mymusic.presentation.home.trending

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
// Note: do not import foundation.lazy.items to avoid clashing with paging compose 'items'
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
// Use index-based items() to avoid paging-compose items import conflicts
import coil.compose.AsyncImage
import com.example.mymusic.domain.model.Track
import com.example.mymusic.playback.PlaybackController
import com.example.mymusic.presentation.components.AppEmpty
import com.example.mymusic.presentation.components.AppError
import com.example.mymusic.presentation.components.AppLoading
import com.example.mymusic.presentation.components.*
import com.example.mymusic.ui.theme.JetcasterSpacing
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Composable
fun TrendingScreen(
    viewModel: TrendingViewModel = hiltViewModel(),
    navController: androidx.navigation.NavController? = null
) {
    val items: LazyPagingItems<Track> = viewModel.pagingData.collectAsLazyPagingItems()
    
    // Update loaded tracks when paging data changes
    LaunchedEffect(items.itemSnapshotList) {
        val tracks = items.itemSnapshotList.items
        viewModel.updateLoadedTracks(tracks)
    }
    
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Trending Tracks",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(JetcasterSpacing.md)
                .accessibleHeading(level = 1)
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = JetcasterSpacing.md)
        ) {
            val count = items.itemCount
            items(count) { index ->
                val track = items[index]
                if (track != null) TrackRow(
                    track, 
                    onClick = { 
                        viewModel.onTrackClicked(track)
                        // Navigate to track detail if navController is available
                        Log.d("TrendingScreen", "Navigating to track detail: ${track.id} - ${track.title}")
                        navController?.navigate("track_detail/${track.id}")
                    }
                )
            }
            items.apply {
                when {
                    loadState.refresh is androidx.paging.LoadState.Loading -> {
                        item { AppLoading() }
                    }
                    loadState.refresh is androidx.paging.LoadState.NotLoading && count == 0 -> {
                        item { AppEmpty(onRetry = { refresh() }) }
                    }
                    loadState.append is androidx.paging.LoadState.Loading -> {
                        item { AppLoading(fullscreen = false) }
                    }
                    loadState.refresh is androidx.paging.LoadState.Error -> {
                        val e = loadState.refresh as androidx.paging.LoadState.Error
                        item { AppError(message = e.error.message ?: "Error", onRetry = { retry() }) }
                    }
                    loadState.append is androidx.paging.LoadState.Error -> {
                        val e = loadState.append as androidx.paging.LoadState.Error
                        item { AppError(message = e.error.message ?: "Error", onRetry = { retry() }) }
                    }
                }
            }
        }
    }
}

@Composable
private fun TrackRow(track: Track, onClick: () -> Unit = {}) {
    AnimatedListItem(visible = true) {
        ListItem(
            headlineContent = { 
                Text(
                    text = track.title,
                    style = MaterialTheme.typography.titleMedium
                ) 
            },
            supportingContent = { 
                Text(
                    text = track.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ) 
            },
            leadingContent = {
                AsyncImage(
                    model = track.artworkUrl,
                    contentDescription = "Artwork for ${track.title} by ${track.artist}",
                    modifier = Modifier
                        .size(56.dp)
                        .accessibleImage("Artwork for ${track.title} by ${track.artist}")
                )
            },
            modifier = Modifier
                .accessibleClickable(
                    onClickLabel = "Play ${track.title} by ${track.artist}",
                    role = androidx.compose.ui.semantics.Role.Button
                ) { onClick() }
                .heightIn(min = 72.dp)
                .accessibleTouchTarget(),
            trailingContent = {
                Text(
                    text = formatDurationMsToMinSec(track.durationMs),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )
    }
}

private fun formatDurationMsToMinSec(durationMs: Long): String {
    if (durationMs <= 0L) return "00:00"
    val totalSeconds = durationMs / 1000L
    val minutes = totalSeconds / 60L
    val seconds = totalSeconds % 60L
    val minStr = minutes.coerceAtLeast(0).toString().padStart(2, '0')
    val secStr = seconds.coerceAtLeast(0).toString().padStart(2, '0')
    return "$minStr:$secStr"
}

// Replaced local state views with unified components: AppLoading, AppEmpty, AppError


