package com.example.mymusic.domain.usecase

import com.example.mymusic.data.local.DownloadStatus
import com.example.mymusic.domain.repository.DownloadRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDownloadProgressUseCase @Inject constructor(
    private val downloadRepository: DownloadRepository
) {
    
    fun getDownloadStatus(trackId: String): Flow<DownloadStatus?> {
        return downloadRepository.getDownloadStatus(trackId)
    }
    
    fun getDownloadProgress(trackId: String): Flow<Int> {
        return downloadRepository.getDownloadProgress(trackId)
    }
    
    fun isTrackDownloaded(trackId: String): Flow<Boolean> {
        return downloadRepository.isTrackDownloaded(trackId)
    }
}
