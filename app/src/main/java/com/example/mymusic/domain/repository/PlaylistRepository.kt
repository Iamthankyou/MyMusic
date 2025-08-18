package com.example.mymusic.domain.repository

import com.example.mymusic.domain.model.Playlist

interface PlaylistRepository {
    suspend fun getTrendingPlaylists(limit: Int, offset: Int): List<Playlist>
    suspend fun getPlaylistDetail(playlistId: String): Playlist?
}
