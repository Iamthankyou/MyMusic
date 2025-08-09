package com.example.mymusic.domain.model

data class Track(
    val id: String,
    val title: String,
    val artist: String,
    val durationMs: Long,
    val artworkUrl: String?,
    val audioUrl: String?,
    val isDownloadable: Boolean = false
)


