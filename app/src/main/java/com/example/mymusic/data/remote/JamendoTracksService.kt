package com.example.mymusic.data.remote

import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

interface JamendoTracksService {
    
    @GET("tracks/")
    suspend fun searchTracks(
        @Query("query") query: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): SearchResponseDto
    
    // Original trending tracks endpoint (for home screen)
    @GET("tracks/")
    suspend fun getTrendingTracks(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("order") order: String = "popularity_total"
    ): SearchResponseDto
    
    // Discovery endpoints (for explore screen)
    @GET("tracks/")
    suspend fun getDiscoveryTrendingTracks(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("orderby") orderBy: String = "popularity_total"
    ): SearchResponseDto
    
    @GET("tracks/")
    suspend fun getNewReleases(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("orderby") orderBy: String = "releasedate"
    ): SearchResponseDto
    
    @GET("tracks/")
    suspend fun getTracksByGenre(
        @Query("tags") genre: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("orderby") orderBy: String = "popularity_total"
    ): SearchResponseDto
    
    @GET("tracks/")
    suspend fun getFeaturedTracks(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("orderby") orderBy: String = "popularity_total",
        @Query("include") include: String = "musicinfo"
    ): SearchResponseDto
}

@Serializable
data class SearchResponseDto(
    val headers: HeadersDto? = null,
    val results: List<TrackDto> = emptyList()
)

@Serializable
data class HeadersDto(
    val status: String? = null,
    val code: Int? = null,
    val error_message: String? = null,
    val warnings: String? = null,
    val results_count: Int? = null
)

@Serializable
data class TrackDto(
    val id: String = "",
    val name: String = "",
    val duration: Int = 0,
    val artist_id: String = "",
    val artist_name: String = "",
    val album_id: String = "",
    val album_name: String = "",
    val license_ccurl: String = "",
    val position: Int = 0,
    val releasedate: String = "",
    val album_image: String = "",
    val audio: String = "",
    val audiodownload: String = "",
    val prourl: String = "",
    val shorturl: String = "",
    val shareurl: String = "",
    val waveform: String = "",
    val image: String = "",
    val musicinfo: MusicInfoDto? = null,
    val tags: TagsDto? = null
)

@Serializable
data class MusicInfoDto(
    val vocalinstrumental: String? = null,
    val lang: String? = null,
    val gender: String? = null,
    val acousticorchestral: String? = null,
    val speed: String? = null
)

@Serializable
data class TagsDto(
    val genres: List<String>? = null,
    val instruments: List<String>? = null,
    val vartags: List<String>? = null
)


