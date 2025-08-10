package com.example.mymusic.presentation.player

import androidx.compose.foundation.layout.*
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
import coil.compose.AsyncImage
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Slider

@Composable
fun MiniPlayer(
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val state by viewModel.nowPlaying.collectAsState()
    if (state.trackId == null) return
    Surface(tonalElevation = 2.dp, modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = state.artworkUrl,
                    contentDescription = "Now playing artwork",
                    modifier = Modifier.size(48.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = state.title, style = MaterialTheme.typography.bodyLarge, maxLines = 1)
                    Text(text = state.artist, style = MaterialTheme.typography.bodySmall, maxLines = 1)
                }
                IconButton(onClick = { viewModel.toggle() }) {
                    Text(if (state.isPlaying) "Pause" else "Play")
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
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)
                )
            } else {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}


