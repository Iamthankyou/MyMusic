package com.example.mymusic.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mymusic.data.local.DownloadStatus

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
            IconButton(
                onClick = onPlayClick,
                modifier = modifier
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Play downloaded track",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        
        DownloadStatus.DOWNLOADING -> {
            CircularProgressIndicator(
                progress = progress / 100f,
                modifier = modifier.size(24.dp),
                strokeWidth = 2.dp
            )
        }
        
        DownloadStatus.FAILED -> {
            IconButton(
                onClick = onDownloadClick,
                modifier = modifier
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = "Retry download",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        
        DownloadStatus.PENDING -> {
            IconButton(
                onClick = onDownloadClick,
                modifier = modifier
            ) {
                Icon(
                    imageVector = Icons.Default.Pause,
                    contentDescription = "Download pending",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        
        else -> {
            IconButton(
                onClick = onDownloadClick,
                modifier = modifier
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Download track",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
