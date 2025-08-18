package com.example.mymusic.domain.usecase

import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.repository.PlaylistRepository
import javax.inject.Inject

class GetTrendingPlaylistsUseCase @Inject constructor(
    private val playlistRepository: PlaylistRepository
) {
    suspend operator fun invoke(limit: Int, offset: Int): List<Playlist> {
        return playlistRepository.getTrendingPlaylists(limit, offset)
    }
}
