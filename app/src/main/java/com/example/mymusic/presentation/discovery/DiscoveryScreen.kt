package com.example.mymusic.presentation.discovery

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.mymusic.presentation.discovery.components.FeaturedContent
import com.example.mymusic.presentation.discovery.components.GenreBrowser
import com.example.mymusic.presentation.discovery.components.NewReleasesSection
import com.example.mymusic.presentation.discovery.components.TrendingSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoveryScreen(
    viewModel: DiscoveryViewModel = hiltViewModel()
) {
    val trendingTracks by viewModel.trendingTracks.collectAsState()
    val newReleases by viewModel.newReleases.collectAsState()
    val featuredTracks by viewModel.featuredTracks.collectAsState()
    val selectedGenre by viewModel.selectedGenre.collectAsState()
    val genreTracks by viewModel.genreTracks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Debug logging
    LaunchedEffect(trendingTracks, newReleases, featuredTracks, isLoading, error) {
        Log.d("DiscoveryScreen", "State updated: trending=${trendingTracks.size}, newReleases=${newReleases.size}, featured=${featuredTracks.size}, loading=$isLoading, error=$error")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Discover") },
                actions = {
                    IconButton(onClick = { 
                        Log.d("DiscoveryScreen", "Refresh button clicked")
                        viewModel.refresh() 
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Loading discovery content...")
                    }
                }
            }
            error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Error: $error",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.clearError() }) {
                            Text("Retry")
                        }
                    }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Debug info
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Debug Info",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("Trending: ${trendingTracks.size} tracks")
                                Text("New Releases: ${newReleases.size} tracks")
                                Text("Featured: ${featuredTracks.size} tracks")
                                Text("Selected Genre: $selectedGenre")
                                Text("Genre Tracks: ${genreTracks.size} tracks")
                            }
                        }
                    }

                    // Featured Content Section
                    item {
                        Text(
                            text = "Featured (${featuredTracks.size})",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    item {
                        if (featuredTracks.isNotEmpty()) {
                            FeaturedContent(
                                tracks = featuredTracks,
                                onTrackClicked = { track -> viewModel.onTrackClicked(track) }
                            )
                        } else {
                            Text("No featured tracks available", style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    // Trending Section
                    item {
                        Text(
                            text = "Trending Now (${trendingTracks.size})",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    item {
                        if (trendingTracks.isNotEmpty()) {
                            TrendingSection(
                                tracks = trendingTracks,
                                onTrackClicked = { track -> viewModel.onTrackClicked(track) }
                            )
                        } else {
                            Text("No trending tracks available", style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    // New Releases Section
                    item {
                        Text(
                            text = "New Releases (${newReleases.size})",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    item {
                        if (newReleases.isNotEmpty()) {
                            NewReleasesSection(
                                tracks = newReleases,
                                onTrackClicked = { track -> viewModel.onTrackClicked(track) }
                            )
                        } else {
                            Text("No new releases available", style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    // Genre Browser
                    item {
                        Text(
                            text = "Browse by Genre",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    item {
                        GenreBrowser(
                            selectedGenre = selectedGenre,
                            onGenreSelected = { genre -> viewModel.selectGenre(genre) }
                        )
                    }

                    // Genre Tracks (if genre is selected)
                    if (selectedGenre != null) {
                        item {
                            Text(
                                text = "$selectedGenre Tracks (${genreTracks.size})",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        if (genreTracks.isNotEmpty()) {
                            items(genreTracks) { track ->
                                com.example.mymusic.presentation.components.TrackItem(
                                    track = track,
                                    onClick = { viewModel.onTrackClicked(track) }
                                )
                            }
                        } else {
                            item {
                                Text("No tracks found for $selectedGenre", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}
