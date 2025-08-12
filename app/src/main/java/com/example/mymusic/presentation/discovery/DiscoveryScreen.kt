package com.example.mymusic.presentation.discovery

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mymusic.domain.model.Track
import com.example.mymusic.presentation.components.TrackItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoveryScreen(
    onTrackClicked: (Track) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DiscoveryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Discover",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.refresh() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Featured Content Section
            item {
                FeaturedContentSection(
                    tracks = uiState.featuredContent,
                    isLoading = uiState.isLoadingFeatured,
                    onTrackClicked = onTrackClicked
                )
            }
            
            // Trending Tracks Section
            item {
                TrendingSection(
                    tracks = uiState.trendingTracks,
                    isLoading = uiState.isLoadingTrending,
                    onTrackClicked = onTrackClicked
                )
            }
            
            // New Releases Section
            item {
                NewReleasesSection(
                    tracks = uiState.newReleases,
                    isLoading = uiState.isLoadingNewReleases,
                    onTrackClicked = onTrackClicked
                )
            }
            
            // Genre Browser Section
            item {
                GenreBrowserSection(
                    onGenreSelected = { genre ->
                        viewModel.loadTracksByGenre(genre)
                    }
                )
            }
        }
    }
}

@Composable
private fun FeaturedContentSection(
    tracks: List<Track>,
    isLoading: Boolean,
    onTrackClicked: (Track) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Featured",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        if (isLoading) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(5) {
                    SkeletonTrackCard()
                }
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tracks) { track ->
                    TrackItem(
                        track = track,
                        onClick = { onTrackClicked(track) },
                        modifier = Modifier.width(200.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TrendingSection(
    tracks: List<Track>,
    isLoading: Boolean,
    onTrackClicked: (Track) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Trending Now",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        if (isLoading) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(5) {
                    SkeletonTrackCard()
                }
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tracks) { track ->
                    TrackItem(
                        track = track,
                        onClick = { onTrackClicked(track) },
                        modifier = Modifier.width(200.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun NewReleasesSection(
    tracks: List<Track>,
    isLoading: Boolean,
    onTrackClicked: (Track) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "New Releases",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        if (isLoading) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(5) {
                    SkeletonTrackCard()
                }
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tracks) { track ->
                    TrackItem(
                        track = track,
                        onClick = { onTrackClicked(track) },
                        modifier = Modifier.width(200.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun GenreBrowserSection(
    onGenreSelected: (String) -> Unit
) {
    val genres = listOf("Rock", "Pop", "Jazz", "Classical", "Electronic", "Hip Hop", "Country", "Blues")
    
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Browse by Genre",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(genres) { genre ->
                FilterChip(
                    onClick = { onGenreSelected(genre) },
                    label = { Text(genre) },
                    selected = false,
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
            }
        }
    }
}

@Composable
private fun SkeletonTrackCard() {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        // Skeleton content
    }
}
