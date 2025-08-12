package com.example.mymusic.presentation.player

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Slider

@Composable
fun MiniPlayer(
    modifier: Modifier = Modifier,
    navController: NavController? = null,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val state by viewModel.nowPlaying.collectAsState()
    if (state.trackId == null) return
    Surface(tonalElevation = 2.dp, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable(
                    enabled = navController != null,
                    onClick = { navController?.navigate("player") }
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = state.artworkUrl,
                    contentDescription = "Now playing: ${state.title} by ${state.artist}",
                    modifier = Modifier.size(48.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = state.title, style = MaterialTheme.typography.bodyLarge, maxLines = 1)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = state.artist, style = MaterialTheme.typography.bodySmall, maxLines = 1)
                        
                        // Show playback speed if not 1.0x
                        val playbackSpeed by viewModel.playbackSpeed.collectAsState()
                        if (playbackSpeed != 1.0f) {
                            Text(
                                text = "• ${playbackSpeed}x",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        // Show repeat mode if enabled
                        val repeatMode by viewModel.repeatMode.collectAsState()
                        if (repeatMode != androidx.media3.common.Player.REPEAT_MODE_OFF) {
                            Text(
                                text = "• 🔄",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        // Show shuffle mode if enabled
                        val shuffleMode by viewModel.shuffleMode.collectAsState()
                        if (shuffleMode) {
                            Text(
                                text = "• 🔀",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
                IconButton(
                    onClick = { viewModel.toggle() },
                    modifier = Modifier.size(48.dp) // Ensure minimum touch target
                ) {
                    Text(
                        text = if (state.isPlaying) "⏸" else "▶",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
            val duration = state.durationMs
            if (duration > 0L) {
                val progress = (state.positionMs.toFloat() / duration.toFloat()).coerceIn(0f, 1f)
                Slider(
                    value = progress,
                    onValueChange = { fraction ->
                        val target = (fraction * duration.toFloat()).toLong()
                        viewModel.seekTo(target)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .heightIn(min = 48.dp) // Ensure minimum touch target height
                )
            } else {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}


