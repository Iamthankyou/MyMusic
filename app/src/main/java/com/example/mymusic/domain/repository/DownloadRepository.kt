package com.example.mymusic.domain.repository

import com.example.mymusic.data.local.DownloadEntity
import com.example.mymusic.data.local.DownloadStatus
import com.example.mymusic.data.local.DownloadPriority
import com.example.mymusic.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {
    
    // Download Management
    suspend fun downloadTrack(track: Track, priority: DownloadPriority = DownloadPriority.NORMAL): Result<Long>
    suspend fun cancelDownload(trackId: String): Result<Unit>
    suspend fun pauseDownload(trackId: String): Result<Unit>
    suspend fun resumeDownload(trackId: String): Result<Unit>
    suspend fun deleteDownload(trackId: String): Result<Unit>
    
    // Internal database update methods (used by services)
    suspend fun updateDownloadId(trackId: String, downloadId: Long): Result<Unit>
    suspend fun updateDownloadStatus(trackId: String, status: DownloadStatus, progress: Int): Result<Unit>
    
    // Download Status
    fun getDownloadStatus(trackId: String): Flow<DownloadStatus?>
    fun getDownloadProgress(trackId: String): Flow<Int>
    fun isTrackDownloaded(trackId: String): Flow<Boolean>
    
    // Download Lists
    fun getAllDownloads(): Flow<List<DownloadEntity>>
    fun getActiveDownloads(): Flow<List<DownloadEntity>>
    fun getCompletedDownloads(): Flow<List<DownloadEntity>>
    fun getDownloadsByStatus(status: DownloadStatus): Flow<List<DownloadEntity>>
    
    // Download Queue Management
    suspend fun updateDownloadPriority(trackId: String, priority: DownloadPriority): Result<Unit>
    suspend fun reorderDownloads(downloadIds: List<String>): Result<Unit>
    suspend fun clearFailedDownloads(): Result<Unit>
    
    // Storage Management
    suspend fun getTotalDownloadedSize(): Long
    suspend fun getCompletedDownloadsCount(): Int
    suspend fun cleanupOldDownloads(): Result<Unit>
    
    // Search and Filter
    fun searchDownloads(query: String): Flow<List<DownloadEntity>>
    fun getDownloadsByArtist(artist: String): Flow<List<DownloadEntity>>
    fun getDownloadsByAlbum(album: String): Flow<List<DownloadEntity>>
}
