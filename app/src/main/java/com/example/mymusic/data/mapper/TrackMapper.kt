package com.example.mymusic.data.mapper

import com.example.mymusic.data.remote.TrackDto
import com.example.mymusic.domain.model.Track
import com.example.mymusic.domain.model.TrackSource

object TrackMapper {
    fun fromDto(dto: TrackDto): Track = Track(
        id = dto.id,
        title = dto.name,
        artist = dto.artist_name,
        durationMs = dto.duration.toLong().times(1000),
        artworkUrl = dto.image.ifEmpty { dto.album_image },
        audioUrl = dto.audio,
        isDownloadable = dto.audiodownload.isNotEmpty(),
        source = TrackSource.JAMENDO
    )

    fun fromDeezerDto(dto: com.example.mymusic.data.remote.DeezerTrackDto): Track = Track(
        id = dto.id.toString(),
        title = dto.title,
        artist = dto.artist?.name ?: "",
        durationMs = dto.duration.toLong().times(1000),
        artworkUrl = dto.album?.coverXl ?: dto.album?.coverBig ?: dto.album?.cover,
        audioUrl = dto.preview,
        isDownloadable = false,
        source = TrackSource.DEEZER
    )
}


