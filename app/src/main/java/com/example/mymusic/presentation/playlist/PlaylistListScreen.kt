package com.example.mymusic.presentation.playlist

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
import com.example.mymusic.presentation.playlist.components.PlaylistItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistListScreen(
    viewModel: PlaylistListViewModel = hiltViewModel(),
    navController: androidx.navigation.NavController? = null
) {
    val playlists by viewModel.playlists.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Debug logging
    LaunchedEffect(playlists, isLoading, error) {
        Log.d("PlaylistListScreen", "State updated: playlists=${playlists.size}, loading=$isLoading, error=$error")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Playlists") },
                actions = {
                    IconButton(onClick = { 
                        Log.d("PlaylistListScreen", "Refresh button clicked")
                        viewModel.loadPlaylists() 
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
                        Text("Loading playlists...")
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
                    verticalArrangement = Arrangement.spacedBy(16.dp)
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
                                Text("Playlists: ${playlists.size} playlists")
                            }
                        }
                    }

                    // Playlists Section
                    item {
                        Text(
                            text = "Trending Playlists (${playlists.size})",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    if (playlists.isNotEmpty()) {
                        items(playlists) { playlist ->
                            PlaylistItem(
                                playlist = playlist,
                                onPlaylistClicked = { 
                                    Log.d("PlaylistListScreen", "Playlist clicked: ${playlist.id}")
                                    navController?.navigate("playlist_detail/${playlist.id}")
                                }
                            )
                        }
                    } else {
                        item {
                            Text(
                                "No playlists available", 
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(vertical = 32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
