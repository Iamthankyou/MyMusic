package com.example.mymusic.domain.model

data class Playlist(
    val id: String,
    val title: String,
    val description: String?,
    val artworkUrl: String?,
    val trackCount: Int,
    val creator: String?,
    val tracks: List<Track> = emptyList()
)
