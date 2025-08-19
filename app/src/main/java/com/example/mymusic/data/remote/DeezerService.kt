package com.example.mymusic.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

interface DeezerService {
	@GET("search")
	suspend fun searchTracks(
		@Query("q") query: String,
		@Query("limit") limit: Int = 20,
		@Query("index") index: Int = 0
	): DeezerSearchResponseDto

	// Deezer chart "global" trending
	@GET("chart/0/tracks")
	suspend fun getTrendingTracks(
		@Query("limit") limit: Int = 20,
		@Query("index") index: Int = 0
	): DeezerSearchResponseDto
}

@Serializable
data class DeezerSearchResponseDto(
	val data: List<DeezerTrackDto> = emptyList(),
	val total: Int? = null,
	val next: String? = null
)

@Serializable
data class DeezerTrackDto(
	val id: Long = 0,
	val title: String = "",
	val duration: Int = 0,
	val preview: String? = null,
	val link: String? = null,
	val artist: DeezerArtistDto? = null,
	val album: DeezerAlbumDto? = null
)

@Serializable
data class DeezerArtistDto(
	val id: Long = 0,
	val name: String = ""
)

@Serializable
data class DeezerAlbumDto(
	val id: Long = 0,
	val title: String = "",
	@SerialName("cover") val cover: String? = null,
	@SerialName("cover_big") val coverBig: String? = null,
	@SerialName("cover_xl") val coverXl: String? = null
)


