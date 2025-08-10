package com.example.mymusic.playback

data class NowPlayingState(
    val trackId: String? = null,
    val title: String = "",
    val artist: String = "",
    val artworkUrl: String? = null,
    val isPlaying: Boolean = false,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L
)


