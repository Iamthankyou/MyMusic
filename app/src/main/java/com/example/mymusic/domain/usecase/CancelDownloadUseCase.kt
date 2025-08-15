package com.example.mymusic.domain.usecase

import com.example.mymusic.domain.repository.DownloadRepository
import javax.inject.Inject

class CancelDownloadUseCase @Inject constructor(
    private val downloadRepository: DownloadRepository
) {
    suspend operator fun invoke(trackId: String): Result<Unit> {
        return downloadRepository.cancelDownload(trackId)
    }
}
