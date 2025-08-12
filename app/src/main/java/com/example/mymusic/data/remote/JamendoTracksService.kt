package com.example.mymusic.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

interface JamendoTracksService {
    @GET("tracks")
    suspend fun getTrendingTracks(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("order") order: String = "popularity_total"
    ): JamendoResponse<TrackDto>

    @GET("tracks")
    suspend fun getNewReleases(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("order") order: String = "releasedate"
    ): JamendoResponse<TrackDto>

    @GET("tracks")
    suspend fun searchTracks(
        @Query("search") query: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("order") order: String = "popularity_total"
    ): JamendoResponse<TrackDto>
}

@Serializable
data class JamendoResponse<T>(
    @SerialName("results") val results: List<T> = emptyList()
)

@Serializable
data class TrackDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String? = null,
    @SerialName("artist_name") val artistName: String? = null,
    @SerialName("duration") val durationSec: Int? = null,
    @SerialName("audio") val audioUrl: String? = null,
    @SerialName("image") val imageUrl: String? = null
)


