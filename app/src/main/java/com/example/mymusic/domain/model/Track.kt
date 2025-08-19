package com.example.mymusic.domain.model

enum class TrackSource { JAMENDO, DEEZER }

data class Track(
    val id: String,
    val title: String,
    val artist: String,
    val durationMs: Long,
    val artworkUrl: String?,
    val audioUrl: String?,
    val isDownloadable: Boolean = false,
    val source: TrackSource? = null
)


