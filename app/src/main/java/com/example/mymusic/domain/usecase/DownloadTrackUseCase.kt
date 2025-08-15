package com.example.mymusic.domain.usecase

import com.example.mymusic.domain.model.Track
import com.example.mymusic.domain.repository.DownloadRepository
import com.example.mymusic.data.local.DownloadPriority
import javax.inject.Inject

class DownloadTrackUseCase @Inject constructor(
    private val downloadRepository: DownloadRepository
) {
    
    suspend operator fun invoke(
        track: Track,
        priority: DownloadPriority = DownloadPriority.NORMAL
    ): Result<Long> {
        return try {
            downloadRepository.downloadTrack(track, priority)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
