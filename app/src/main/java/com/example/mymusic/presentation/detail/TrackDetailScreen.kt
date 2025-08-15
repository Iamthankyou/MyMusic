package com.example.mymusic.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mymusic.presentation.components.TrackItem
import com.example.mymusic.presentation.components.DownloadButton
import com.example.mymusic.presentation.detail.components.RelatedContent
import com.example.mymusic.presentation.detail.components.ShareButton
import com.example.mymusic.data.local.DownloadStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackDetailScreen(
    trackId: String,
    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val trackDetail by viewModel.trackDetail.collectAsState()
    val relatedTracks by viewModel.relatedTracks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    LaunchedEffect(trackId) {
        viewModel.loadTrackDetail(trackId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Track Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    trackDetail?.let { track ->
                        ShareButton(
                            title = track.title,
                            artist = track.artist,
                            url = track.audioUrl ?: ""
                        )
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
                    CircularProgressIndicator()
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
            trackDetail != null -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        trackDetail?.let { track ->
                            // Get download status as Flow for real-time updates
                            val downloadStatusFlow = remember(track.id) {
                                viewModel.getDownloadStatusFlow(track.id)
                            }
                            val downloadStatus by downloadStatusFlow.collectAsState(initial = null)
                            
                            TrackDetailHeader(
                                track = track,
                                onPlayClick = { viewModel.onTrackClicked(track) },
                                onDownloadClick = { viewModel.onDownloadClicked(track) },
                                downloadStatus = downloadStatus
                            )
                        }
                    }
                    
                    if (relatedTracks.isNotEmpty()) {
                        item {
                            Text(
                                text = "Related Tracks",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        item {
                            RelatedContent(
                                tracks = relatedTracks,
                                onTrackClicked = { viewModel.onTrackClicked(it) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TrackDetailHeader(
    track: com.example.mymusic.domain.model.Track,
    onPlayClick: () -> Unit,
    onDownloadClick: () -> Unit,
    downloadStatus: DownloadStatus?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = track.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = track.artist,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Duration: ${track.durationMs / 1000 / 60}:${String.format("%02d", (track.durationMs / 1000) % 60)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Download button with status
                    DownloadButton(
                        downloadStatus = downloadStatus,
                        onDownloadClick = onDownloadClick,
                        onPlayClick = onPlayClick,
                        modifier = Modifier.size(48.dp)
                    )
                    
                    // Play button
                    IconButton(
                        onClick = onPlayClick,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}
