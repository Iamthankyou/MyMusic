package com.example.mymusic.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.mymusic.data.local.DownloadStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@Composable
fun DownloadButton(
    downloadStatus: DownloadStatus?,
    progress: Int = 0,
    onDownloadClick: () -> Unit,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (downloadStatus) {
        DownloadStatus.COMPLETED -> {
            // Success animation with scale effect
            var isAnimating by remember { mutableStateOf(false) }
            val scale by animateFloatAsState(
                targetValue = if (isAnimating) 1.2f else 1f,
                animationSpec = tween(300, easing = FastOutSlowInEasing),
                label = "success_scale"
            )
            
            LaunchedEffect(Unit) {
                isAnimating = true
                delay(100)
                isAnimating = false
            }
            
            IconButton(
                onClick = onPlayClick,
                modifier = modifier.scale(scale)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Play downloaded track",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        DownloadStatus.DOWNLOADING -> {
            // Rotating progress indicator with pulse
            val infiniteTransition = rememberInfiniteTransition(label = "download_animation")
            val rotation by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "rotation"
            )
            
            val pulse by infiniteTransition.animateFloat(
                initialValue = 0.8f,
                targetValue = 1.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulse"
            )
            
            CircularProgressIndicator(
                progress = progress / 100f,
                modifier = modifier
                    .size(24.dp)
                    .scale(pulse)
                    .graphicsLayer(rotationZ = rotation),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
        
        DownloadStatus.FAILED -> {
            // Error state with shake animation
            var isShaking by remember { mutableStateOf(false) }
            val shakeOffset by animateFloatAsState(
                targetValue = if (isShaking) 1f else 0f,
                animationSpec = tween(500, easing = FastOutSlowInEasing),
                label = "shake"
            )
            
            LaunchedEffect(Unit) {
                isShaking = true
                delay(500)
                isShaking = false
            }
            
            IconButton(
                onClick = onDownloadClick,
                modifier = modifier.graphicsLayer(
                    translationX = shakeOffset * 8f
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = "Retry download",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
        
        DownloadStatus.PENDING -> {
            // Pending state with subtle pulse
            val infiniteTransition = rememberInfiniteTransition(label = "pending_animation")
            val alpha by infiniteTransition.animateFloat(
                initialValue = 0.6f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1500, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "alpha"
            )
            
            IconButton(
                onClick = onDownloadClick,
                modifier = modifier.graphicsLayer(alpha = alpha)
            ) {
                Icon(
                    imageVector = Icons.Default.Pause,
                    contentDescription = "Download pending",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        else -> {
            // Default download state with click animation
            var isPressed by remember { mutableStateOf(false) }
            val scale by animateFloatAsState(
                targetValue = if (isPressed) 0.9f else 1f,
                animationSpec = tween(150, easing = FastOutSlowInEasing),
                label = "click_scale"
            )
            
            IconButton(
                onClick = {
                    isPressed = true
                    onDownloadClick()
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(150)
                        isPressed = false
                    }
                },
                modifier = modifier.scale(scale)
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Download track",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
