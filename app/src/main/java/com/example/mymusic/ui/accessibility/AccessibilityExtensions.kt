package com.example.mymusic.ui.accessibility

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/**
 * Accessibility utilities for ensuring WCAG compliance
 */

/**
 * Minimum touch target size as per Material Design and WCAG guidelines
 */
val MinTouchTargetSize = 48.dp

/**
 * Ensures a composable meets minimum touch target requirements
 */
fun Modifier.minTouchTarget() = this.then(
    Modifier
        .widthIn(min = MinTouchTargetSize)
        .heightIn(min = MinTouchTargetSize)
)

/**
 * Adds semantic content description for accessibility services
 */
fun Modifier.accessibilityDescription(description: String) = this.then(
    Modifier.semantics {
        contentDescription = description
    }
)

/**
 * Common content descriptions for music app components
 */
object MusicContentDescriptions {
    fun trackArtwork(title: String, artist: String) = "Album artwork for $title by $artist"
    fun playButton(isPlaying: Boolean) = if (isPlaying) "Pause" else "Play"
    fun trackItem(title: String, artist: String) = "Play $title by $artist"
    fun navigationTab(tabName: String) = "Navigate to $tabName"
    fun seekSlider(currentTime: String, totalTime: String) = "Seek to position in track. Current: $currentTime, Total: $totalTime"
}

/**
 * Formats duration in milliseconds to accessible time description
 */
fun formatDurationForAccessibility(durationMs: Long): String {
    if (durationMs <= 0L) return "0 seconds"
    val totalSeconds = durationMs / 1000L
    val minutes = totalSeconds / 60L
    val seconds = totalSeconds % 60L
    
    return when {
        minutes > 0 -> "$minutes minutes and $seconds seconds"
        else -> "$seconds seconds"
    }
}
