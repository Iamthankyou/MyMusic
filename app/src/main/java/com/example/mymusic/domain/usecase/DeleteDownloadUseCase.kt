package com.example.mymusic.domain.usecase

import com.example.mymusic.domain.repository.DownloadRepository
import javax.inject.Inject

class DeleteDownloadUseCase @Inject constructor(
    private val downloadRepository: DownloadRepository
) {
    
    suspend operator fun invoke(trackId: String): Result<Unit> {
        return try {
            downloadRepository.deleteDownload(trackId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
