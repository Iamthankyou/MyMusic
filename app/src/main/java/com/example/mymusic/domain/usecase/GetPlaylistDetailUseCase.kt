package com.example.mymusic.domain.usecase

import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.repository.PlaylistRepository
import javax.inject.Inject

class GetPlaylistDetailUseCase @Inject constructor(
    private val playlistRepository: PlaylistRepository
) {
    suspend operator fun invoke(playlistId: String): Playlist? {
        return playlistRepository.getPlaylistDetail(playlistId)
    }
}
