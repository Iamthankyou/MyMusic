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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
// Use index-based items() to avoid paging-compose items import conflicts
import coil.compose.AsyncImage
import com.example.mymusic.domain.model.Track
import com.example.mymusic.data.local.DownloadStatus
import com.example.mymusic.playback.PlaybackController
import com.example.mymusic.presentation.components.AppEmpty
import com.example.mymusic.presentation.components.AppError
import com.example.mymusic.presentation.components.AppLoading
import com.example.mymusic.presentation.components.*
import com.example.mymusic.ui.theme.JetcasterSpacing
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import android.net.Uri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingScreen(
    viewModel: TrendingViewModel = hiltViewModel(),
    navController: androidx.navigation.NavController? = null
) {
    val items: LazyPagingItems<Track> = viewModel.pagingData.collectAsLazyPagingItems()
    var homeQuery by androidx.compose.runtime.remember { mutableStateOf("") }
    val presetTags = listOf("Pop", "Rock", "Hip-Hop", "Chill", "Workout", "Lo-fi")
    
    // Collect ViewModel states
    val currentGenre by viewModel.currentGenre.collectAsState()
    val isSearchingByGenre by viewModel.isSearchingByGenre.collectAsState()
    val genreSearchResults by viewModel.genreSearchResults.collectAsState()
    
    // Collect slides feed states
    val topTracks by viewModel.topTracks.collectAsState()
    val latestTracks by viewModel.latestTracks.collectAsState()
    val chillTracks by viewModel.chillTracks.collectAsState()
    val rockTracks by viewModel.rockTracks.collectAsState()
    val electronicTracks by viewModel.electronicTracks.collectAsState()
    val jazzTracks by viewModel.jazzTracks.collectAsState()
    val acousticTracks by viewModel.acousticTracks.collectAsState()
    val isLoadingSlides by viewModel.isLoadingSlides.collectAsState()
    
    // Update loaded tracks when paging data changes
    LaunchedEffect(items.itemSnapshotList) {
        val tracks = items.itemSnapshotList.items
        viewModel.updateLoadedTracks(tracks)
    }
    
    // Scroll to top when genre changes
    LaunchedEffect(currentGenre) {
        // This will trigger a recomposition and scroll to top
    }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp) // Add bottom padding for mini player
    ) {
        // Search bar on Home
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = JetcasterSpacing.md, vertical = JetcasterSpacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchBar(
                    query = homeQuery,
                    onQueryChange = { homeQuery = it },
                    onSearch = {
                        val q = homeQuery.trim()
                        if (q.isNotEmpty()) {
                            val encoded = Uri.encode(q)
                            navController?.navigate("search?query=$encoded")
                        } else {
                            navController?.navigate("search")
                        }
                    },
                    active = false,
                    onActiveChange = { active ->
                        if (active) navController?.navigate("search")
                    },
                    placeholder = { Text("Search for tracks, artists...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    modifier = Modifier.weight(1f)
                ) {}
            }
        }

        // Slides Feed Section - sắp xếp hợp lý theo thứ tự ưu tiên
        if (!isLoadingSlides) {
                            // Top Tracks Slides - lớn nhất, ở đầu
                if (topTracks.isNotEmpty()) {
                    item {
                        SlidesFeed(
                            title = "Top Tracks",
                            tracks = topTracks,
                            onTrackClick = { track ->
                                viewModel.onTrackClicked(track)
                            }
                        )
                    }
                }
            
            // Latest Releases Slides - thon hơn, không có page indicator
                            if (latestTracks.isNotEmpty()) {
                    item {
                        LatestTracksSlidesFeed(
                            title = "Latest Releases",
                            tracks = latestTracks,
                            onTrackClick = { track ->
                                viewModel.onTrackClicked(track)
                            }
                        )
                    }
                }
            
            // Mood-based Slides - nhỏ nhất, ở cuối
                            if (chillTracks.isNotEmpty()) {
                    item {
                        CompactSlidesFeed(
                            title = "Chill Vibes",
                            tracks = chillTracks,
                            onTrackClick = { track ->
                                viewModel.onTrackClicked(track)
                            }
                        )
                    }
                }

                // Rock Music Feed
                if (rockTracks.isNotEmpty()) {
                    item {
                        CompactSlidesFeed(
                            title = "Rock Hits",
                            tracks = rockTracks,
                            onTrackClick = { track ->
                                viewModel.onTrackClicked(track)
                            }
                        )
                    }
                }

                // Electronic Music Feed
                if (electronicTracks.isNotEmpty()) {
                    item {
                        CompactSlidesFeed(
                            title = "Electronic Beats",
                            tracks = electronicTracks,
                            onTrackClick = { track ->
                                viewModel.onTrackClicked(track)
                            }
                        )
                    }
                }

                // Jazz Music Feed
                if (jazzTracks.isNotEmpty()) {
                    item {
                        CompactSlidesFeed(
                            title = "Jazz Classics",
                            tracks = jazzTracks,
                            onTrackClick = { track ->
                                viewModel.onTrackClicked(track)
                            }
                        )
                    }
                }

                // Acoustic Music Feed
                if (acousticTracks.isNotEmpty()) {
                    item {
                        CompactSlidesFeed(
                            title = "Acoustic Sessions",
                            tracks = acousticTracks,
                            onTrackClick = { track ->
                                viewModel.onTrackClicked(track)
                            }
                        )
                    }
                }
        } else {
            // Loading state for slides
            item { AppLoading() }
        }

        // Preset tags
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = JetcasterSpacing.md),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(presetTags) { tag ->
                    AssistChip(
                        onClick = {
                            if (currentGenre == tag) {
                                // If same genre is clicked again, clear the search
                                viewModel.clearGenreSearch()
                            } else {
                                // Search by genre
                                viewModel.searchByGenre(tag)
                            }
                        },
                        label = { Text(tag) },
                        colors = androidx.compose.material3.AssistChipDefaults.assistChipColors(
                            containerColor = if (currentGenre == tag) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            },
                            labelColor = if (currentGenre == tag) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(JetcasterSpacing.md))
        }

        // Dynamic title based on current state
        val title = when {
            currentGenre != null -> "$currentGenre Tracks"
            else -> "Trending Tracks"
        }
        
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = JetcasterSpacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.accessibleHeading(level = 1)
                    )
                    
                    // Show result count for genre search
                    if (currentGenre != null && !isSearchingByGenre) {
                        Text(
                            text = "${genreSearchResults.size} tracks found",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                // Show back button when viewing genre results
                if (currentGenre != null) {
                    androidx.compose.material3.TextButton(
                        onClick = { viewModel.clearGenreSearch() }
                    ) {
                        Text("Back to Trending")
                    }
                }
            }
        }

        // Show genre search results or trending tracks
        if (currentGenre != null) {
            // Show genre search results
            if (isSearchingByGenre) {
                item { AppLoading() }
            } else if (genreSearchResults.isEmpty()) {
                item {
                    AppEmpty(
                        message = "No tracks found for $currentGenre",
                        onRetry = { viewModel.searchByGenre(currentGenre!!) }
                    )
                }
            } else {
                items(genreSearchResults) { track ->
                    // Get download status as Flow for real-time updates
                    val downloadStatusFlow = remember(track.id) {
                        viewModel.getDownloadStatusFlow(track.id)
                    }
                    val downloadStatus by downloadStatusFlow.collectAsState(initial = null)
                    
                    TrackItem(
                        track = track,
                        onClick = { 
                            viewModel.onTrackClicked(track)
                            // Navigate to track detail if navController is available
                            Log.d("TrendingScreen", "Navigating to track detail: ${track.id} - ${track.title}")
                            navController?.navigate("track_detail/${track.id}")
                        },
                        downloadStatus = downloadStatus,
                        onDownloadClick = {
                            viewModel.onDownloadClicked(track)
                        }
                    )
                }
            }
        } else {
            // Show trending tracks
            val count = items.itemCount
            items(count) { index ->
                val track = items[index]
                if (track != null) {
                    // Get download status as Flow for real-time updates
                    val downloadStatusFlow = remember(track.id) {
                        viewModel.getDownloadStatusFlow(track.id)
                    }
                    val downloadStatus by downloadStatusFlow.collectAsState(initial = null)
                    
                    TrackItem(
                        track = track,
                        onClick = { 
                            viewModel.onTrackClicked(track)
                            // Navigate to track detail if navController is available
                            Log.d("TrendingScreen", "Navigating to track detail: ${track.id} - ${track.title}")
                            navController?.navigate("track_detail/${track.id}")
                        },
                        downloadStatus = downloadStatus,
                        onDownloadClick = {
                            viewModel.onDownloadClicked(track)
                        }
                    )
                }
            }
            
            // Handle paging states
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

// Keep TrackRow for backward compatibility but it's no longer used
@Composable
private fun TrackRow(
    track: Track, 
    onClick: () -> Unit = {},
    onDownloadClick: () -> Unit = {}
) {
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
                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = formatDurationMsToMinSec(track.durationMs),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    IconButton(
                        onClick = onDownloadClick,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Download ${track.title}",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
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


