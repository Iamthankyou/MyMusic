package com.example.mymusic.domain.usecase

import com.example.mymusic.data.local.DownloadEntity
import com.example.mymusic.domain.repository.DownloadRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDownloadedTracksUseCase @Inject constructor(
    private val downloadRepository: DownloadRepository
) {
    
    operator fun invoke(): Flow<List<DownloadEntity>> {
        return downloadRepository.getCompletedDownloads()
    }
}
