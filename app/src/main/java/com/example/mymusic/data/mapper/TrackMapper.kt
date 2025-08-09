package com.example.mymusic.data.mapper

import com.example.mymusic.data.remote.TrackDto
import com.example.mymusic.domain.model.Track

object TrackMapper {
    fun fromDto(dto: TrackDto): Track = Track(
        id = dto.id,
        title = dto.name ?: "",
        artist = dto.artistName ?: "",
        durationMs = (dto.durationSec ?: 0) * 1000L,
        artworkUrl = dto.imageUrl,
        audioUrl = dto.audioUrl,
        isDownloadable = false
    )
}


