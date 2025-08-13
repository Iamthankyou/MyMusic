package com.example.mymusic.domain.model

data class Artist(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val website: String?,
    val joinDate: String?,
    val trackCount: Int = 0,
    val albumCount: Int = 0
)
