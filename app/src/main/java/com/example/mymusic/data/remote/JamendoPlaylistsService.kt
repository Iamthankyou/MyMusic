package com.example.mymusic.data.remote

import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.mymusic.data.remote.HeadersDto
import com.example.mymusic.data.remote.TrackDto

interface JamendoPlaylistsService {
    
    @GET("playlists/")
    suspend fun getTrendingPlaylists(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("orderby") orderBy: String = "popularity_total"
    ): PlaylistResponseDto
    
    @GET("playlists/")
    suspend fun getPlaylistDetail(
        @Query("id") playlistId: String,
        @Query("include") include: String = "tracks"
    ): PlaylistResponseDto
}

@Serializable
data class PlaylistResponseDto(
    val headers: HeadersDto? = null,
    val results: List<PlaylistDto> = emptyList()
)

@Serializable
data class PlaylistDto(
    val id: String = "",
    val name: String = "",
    val creationdate: String = "",
    val user_id: String = "",
    val user_name: String = "",
    val zip: String = "",
    val shorturl: String = "",
    val shareurl: String = ""
)
