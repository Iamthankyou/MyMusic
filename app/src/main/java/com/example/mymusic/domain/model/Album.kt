package com.example.mymusic.domain.model

data class Album(
    val id: String,
    val name: String,
    val artistId: String,
    val artistName: String,
    val releaseDate: String?,
    val imageUrl: String?,
    val trackCount: Int = 0
)
