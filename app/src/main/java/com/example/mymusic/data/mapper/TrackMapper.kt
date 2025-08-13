package com.example.mymusic.data.mapper

import com.example.mymusic.data.remote.TrackDto
import com.example.mymusic.domain.model.Track

object TrackMapper {
    fun fromDto(dto: TrackDto): Track = Track(
        id = dto.id,
        title = dto.name,
        artist = dto.artist_name,
        durationMs = dto.duration.toLong().times(1000),
        artworkUrl = dto.image.ifEmpty { dto.album_image },
        audioUrl = dto.audio,
        isDownloadable = dto.audiodownload.isNotEmpty()
    )
}


