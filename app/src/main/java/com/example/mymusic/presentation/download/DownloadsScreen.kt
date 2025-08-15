package com.example.mymusic.presentation.download

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.mymusic.data.local.DownloadEntity
import com.example.mymusic.data.local.DownloadStatus
import com.example.mymusic.presentation.components.DownloadProgressIndicator
import com.example.mymusic.presentation.components.DownloadStatusBadge
import com.example.mymusic.presentation.components.DownloadProgressBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadsScreen(
    viewModel: DownloadsViewModel = hiltViewModel(),
    onNavigateToPlayer: (String) -> Unit = {}
) {
    val downloadsState by viewModel.downloadsState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Downloads") },
                actions = {
                    // Debug buttons
                    IconButton(onClick = { viewModel.debugDownloads() }) {
                        Icon(Icons.Default.BugReport, "Debug")
                    }
                    IconButton(onClick = { viewModel.openSettings() }) {
                        Icon(Icons.Default.Settings, "Settings")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Search Bar with animation
            item {
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically(
                        initialOffsetY = { -it },
                        animationSpec = tween(500, easing = FastOutSlowInEasing)
                    ) + fadeIn(animationSpec = tween(500))
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.updateSearchQuery(it) },
                        placeholder = { Text("Search downloads...") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Search, "Search") }
                    )
                }
            }
            
            // Active Downloads Section
            if (downloadsState.activeDownloads.isNotEmpty()) {
                item {
                    AnimatedVisibility(
                        visible = true,
                        enter = slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(600, easing = FastOutSlowInEasing)
                        ) + fadeIn(animationSpec = tween(600))
                    ) {
                        Text(
                            text = "Active Downloads",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                itemsIndexed(
                    items = downloadsState.activeDownloads,
                    key = { _, download -> download.trackId }
                ) { index, download ->
                    AnimatedVisibility(
                        visible = true,
                        enter = slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(700 + index * 100, easing = FastOutSlowInEasing)
                        ) + fadeIn(animationSpec = tween(700 + index * 100))
                    ) {
                        ActiveDownloadItem(
                            download = download,
                            onCancel = { viewModel.cancelDownload(download.trackId) },
                            onPlay = { viewModel.playDownloadedTrack(download) }
                        )
                    }
                }
            }
            
            // Completed Downloads Section
            if (downloadsState.completedDownloads.isNotEmpty()) {
                item {
                    AnimatedVisibility(
                        visible = true,
                        enter = slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(800, easing = FastOutSlowInEasing)
                        ) + fadeIn(animationSpec = tween(800))
                    ) {
                        Text(
                            text = "Completed Downloads",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                
                itemsIndexed(
                    items = downloadsState.completedDownloads,
                    key = { _, download -> download.trackId }
                ) { index, download ->
                    AnimatedVisibility(
                        visible = true,
                        enter = slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(900 + index * 100, easing = FastOutSlowInEasing)
                        ) + fadeIn(animationSpec = tween(900 + index * 100))
                    ) {
                        CompletedDownloadItem(
                            download = download,
                            onPlay = { viewModel.playDownloadedTrack(download) },
                            onDelete = { viewModel.deleteDownload(download.trackId) },
                            onShare = { viewModel.shareDownload(download) }
                        )
                    }
                }
            }
            
            // Empty State with animation
            if (downloadsState.activeDownloads.isEmpty() && downloadsState.completedDownloads.isEmpty()) {
                item {
                    AnimatedVisibility(
                        visible = true,
                        enter = scaleIn(
                            animationSpec = tween(1000, easing = FastOutSlowInEasing)
                        ) + fadeIn(animationSpec = tween(1000))
                    ) {
                        EmptyDownloadsState()
                    }
                }
            }
        }
    }
}

@Composable
private fun ActiveDownloadItem(
    download: DownloadEntity,
    onCancel: () -> Unit,
    onPlay: () -> Unit
) {
    // Pulse animation for active downloads
    val infiniteTransition = rememberInfiniteTransition(label = "active_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(pulseScale)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Thumbnail with scale animation
                var isImageLoaded by remember { mutableStateOf(false) }
                val imageScale by animateFloatAsState(
                    targetValue = if (isImageLoaded) 1f else 0.8f,
                    animationSpec = tween(500, easing = FastOutSlowInEasing),
                    label = "image_scale"
                )
                
                AsyncImage(
                    model = download.imageUrl,
                    contentDescription = "Album artwork for ${download.title}",
                    modifier = Modifier
                        .size(56.dp)
                        .scale(imageScale)
                        .padding(end = 12.dp),
                    onSuccess = { isImageLoaded = true }
                )
                
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = download.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = download.artist,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    // Status badge with animation
                    Spacer(modifier = Modifier.height(4.dp))
                    DownloadStatusBadge(
                        status = download.status.name,
                        isActive = true
                    )
                }
                
                // Cancel button with shake animation
                var isShaking by remember { mutableStateOf(false) }
                val shakeOffset by animateFloatAsState(
                    targetValue = if (isShaking) 1f else 0f,
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    label = "shake"
                )
                
                IconButton(
                    onClick = {
                        isShaking = true
                        onCancel()
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(300)
                            isShaking = false
                        }
                    },
                    modifier = Modifier.graphicsLayer(
                        translationX = shakeOffset * 4f
                    )
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Cancel download",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Progress indicator with animation
            DownloadProgressIndicator(
                progress = download.progress / 100f,
                status = "Downloading...",
                speed = null,
                onCancel = onCancel
            )
        }
    }
}

@Composable
private fun CompletedDownloadItem(
    download: DownloadEntity,
    onPlay: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit
) {
    // Success celebration animation
    var isCelebrating by remember { mutableStateOf(false) }
    val celebrationScale by animateFloatAsState(
        targetValue = if (isCelebrating) 1.05f else 1f,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label = "celebration"
    )
    
    LaunchedEffect(Unit) {
        isCelebrating = true
        delay(1000)
        isCelebrating = false
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(celebrationScale)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Thumbnail
                AsyncImage(
                    model = download.imageUrl,
                    contentDescription = "Album artwork for ${download.title}",
                    modifier = Modifier
                        .size(56.dp)
                        .padding(end = 12.dp)
                )
                
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = download.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = download.artist,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Success badge
                    DownloadStatusBadge(
                        status = "Downloaded",
                        isActive = false
                    )
                }
                
                // Success icon with rotation animation
                val infiniteTransition = rememberInfiniteTransition(label = "success_rotation")
                val rotation by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(3000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "success_rotation"
                )
                
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Downloaded",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.graphicsLayer(rotationZ = rotation)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Downloaded • ${formatFileSize(download.fileSize)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Action buttons with scale animation
                    ActionButton(
                        icon = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        onClick = onPlay
                    )
                    
                    ActionButton(
                        icon = Icons.Default.Delete,
                        contentDescription = "Delete",
                        onClick = onDelete
                    )
                    
                    ActionButton(
                        icon = Icons.Default.Share,
                        contentDescription = "Share",
                        onClick = onShare
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = tween(150, easing = FastOutSlowInEasing),
        label = "button_scale"
    )
    
    IconButton(
        onClick = {
            isPressed = true
            onClick()
            CoroutineScope(Dispatchers.Main).launch {
                delay(150)
                isPressed = false
            }
        },
        modifier = Modifier.scale(scale)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun EmptyDownloadsState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Empty icon with bounce animation
        var isBouncing by remember { mutableStateOf(false) }
        val bounceScale by animateFloatAsState(
            targetValue = if (isBouncing) 1.1f else 1f,
            animationSpec = tween(1000, easing = FastOutSlowInEasing),
            label = "bounce"
        )
        
        LaunchedEffect(Unit) {
            isBouncing = true
            delay(500)
            isBouncing = false
        }
        
        Icon(
            imageVector = Icons.Default.Download,
            contentDescription = "No downloads",
            modifier = Modifier
                .size(64.dp)
                .scale(bounceScale),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "No Downloads Yet",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Start downloading your favorite tracks to listen offline",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

private fun formatFileSize(bytes: Long): String {
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${bytes / 1024} KB"
        bytes < 1024 * 1024 * 1024 -> "${bytes / (1024 * 1024)} MB"
        else -> "${bytes / (1024 * 1024 * 1024)} GB"
    }
}
