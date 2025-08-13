package com.example.mymusic.data.mapper

import com.example.mymusic.data.remote.ArtistDto
import com.example.mymusic.domain.model.Artist

object ArtistMapper {
    fun fromDto(dto: ArtistDto): Artist = Artist(
        id = dto.id,
        name = dto.name,
        imageUrl = dto.image.ifEmpty { null },
        website = dto.website.ifEmpty { null },
        joinDate = dto.joindate.ifEmpty { null },
        trackCount = dto.tracks.size,
        albumCount = dto.albums.size
    )
}
