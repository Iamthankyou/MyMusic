package com.example.mymusic.data.mapper

import com.example.mymusic.data.remote.PlaylistDto
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.model.Track

object PlaylistMapper {
    fun fromDto(dto: PlaylistDto): Playlist = Playlist(
        id = dto.id,
        title = dto.name,
        description = null, // Jamendo API không có description
        artworkUrl = null,  // Jamendo API không có artwork cho playlist
        trackCount = 0,     // Không thể biết track count từ API này
        creator = dto.user_name.ifEmpty { null },
        tracks = emptyList() // Không có tracks trong API response
    )
}
