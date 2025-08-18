package com.example.mymusic.data.repository

import com.example.mymusic.data.mapper.PlaylistMapper
import com.example.mymusic.data.remote.JamendoPlaylistsService
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.repository.PlaylistRepository
import javax.inject.Inject

class PlaylistRepositoryImpl @Inject constructor(
    private val service: JamendoPlaylistsService
) : PlaylistRepository {
    
    override suspend fun getTrendingPlaylists(limit: Int, offset: Int): List<Playlist> {
        val response = service.getTrendingPlaylists(limit = limit, offset = offset)
        return response.results.map { PlaylistMapper.fromDto(it) }
    }
    
    override suspend fun getPlaylistDetail(playlistId: String): Playlist? {
        return try {
            val response = service.getPlaylistDetail(playlistId)
            response.results.firstOrNull()?.let { PlaylistMapper.fromDto(it) }
        } catch (e: Exception) {
            null
        }
    }
}
