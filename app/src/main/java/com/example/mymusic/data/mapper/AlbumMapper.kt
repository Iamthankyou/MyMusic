package com.example.mymusic.data.mapper

import com.example.mymusic.data.remote.AlbumDto
import com.example.mymusic.domain.model.Album

object AlbumMapper {
    fun fromDto(dto: AlbumDto): Album = Album(
        id = dto.id,
        name = dto.name,
        artistId = dto.artist_id,
        artistName = dto.artist_name,
        releaseDate = dto.releasedate.ifEmpty { null },
        imageUrl = dto.image.ifEmpty { null },
        trackCount = dto.tracks.size
    )
}
