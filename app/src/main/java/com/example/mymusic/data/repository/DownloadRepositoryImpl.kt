package com.example.mymusic.data.repository

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.example.mymusic.data.download.DownloadService
import com.example.mymusic.data.local.DownloadDao
import com.example.mymusic.data.local.DownloadEntity
import com.example.mymusic.data.local.DownloadStatus
import com.example.mymusic.data.local.DownloadPriority
import com.example.mymusic.domain.model.Track
import com.example.mymusic.domain.repository.DownloadRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDateTime
import javax.inject.Inject

class DownloadRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val downloadDao: DownloadDao
) : DownloadRepository {
    
    companion object {
        private const val TAG = "DownloadRepositoryImpl"
    }
    
    private val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    
    override suspend fun downloadTrack(track: Track, priority: DownloadPriority): Result<Long> {
        return try {
            // Validate track has required fields
            if (track.audioUrl.isNullOrEmpty()) {
                return Result.failure(Exception("Track audio URL is required for download"))
            }
            
            // Check if track is already downloaded
            val existingDownload = withContext(Dispatchers.IO) {
                downloadDao.getDownloadByTrackId(track.id).first()
            }
            if (existingDownload?.status == DownloadStatus.COMPLETED) {
                return Result.failure(Exception("Track already downloaded"))
            }
            
            // Create download entity
            val downloadEntity = DownloadEntity(
                trackId = track.id,
                title = track.title,
                artist = track.artist,
                album = null, // Track doesn't have album
                duration = track.durationMs,
                imageUrl = track.artworkUrl,
                audioUrl = track.audioUrl ?: "",
                localFilePath = getLocalFilePath(track.id),
                fileSize = 0L, // Will be updated when download completes
                status = DownloadStatus.PENDING,
                priority = priority
            )
            
            // Insert into database
            withContext(Dispatchers.IO) {
                downloadDao.insertDownload(downloadEntity)
            }
            
            // Start download service
            val intent = Intent(context, DownloadService::class.java).apply {
                putExtra("track_id", track.id)
                putExtra("track_title", track.title)
                putExtra("track_artist", track.artist)
                putExtra("track_audio_url", track.audioUrl ?: "")
                putExtra(DownloadService.EXTRA_PRIORITY, getPriorityValue(priority))
            }
            context.startService(intent)
            
            Result.success(0L) // Return success, actual download ID will be updated by service
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to download track: ${track.title}", e)
            Result.failure(e)
        }
    }
    
    override suspend fun cancelDownload(trackId: String): Result<Unit> {
        return try {
            val download = withContext(Dispatchers.IO) {
                downloadDao.getDownloadByTrackId(trackId).first()
            }
            download?.downloadId?.let { downloadId ->
                downloadManager.remove(downloadId)
            }
            
            withContext(Dispatchers.IO) {
                downloadDao.updateDownloadStatus(trackId, DownloadStatus.FAILED, 0)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun pauseDownload(trackId: String): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                downloadDao.updateDownloadStatus(trackId, DownloadStatus.PENDING, 0)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun resumeDownload(trackId: String): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                downloadDao.updateDownloadStatus(trackId, DownloadStatus.DOWNLOADING, 0)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteDownload(trackId: String): Result<Unit> {
        return try {
            // Remove from download manager if active
            val download = withContext(Dispatchers.IO) {
                downloadDao.getDownloadByTrackId(trackId).first()
            }
            download?.downloadId?.let { downloadId ->
                downloadManager.remove(downloadId)
            }
            
            // Delete local file
            val localFile = File(getLocalFilePath(trackId))
            if (localFile.exists()) {
                localFile.delete()
            }
            
            // Update database
            withContext(Dispatchers.IO) {
                downloadDao.updateDownloadStatus(trackId, DownloadStatus.DELETED, 0)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun getDownloadStatus(trackId: String): Flow<DownloadStatus?> {
        return downloadDao.getDownloadByTrackId(trackId).map { it?.status }
    }
    
    override fun getDownloadProgress(trackId: String): Flow<Int> {
        return downloadDao.getDownloadByTrackId(trackId).map { it?.progress ?: 0 }
    }
    
    override fun isTrackDownloaded(trackId: String): Flow<Boolean> {
        return downloadDao.getDownloadByTrackId(trackId).map { 
            it?.status == DownloadStatus.COMPLETED 
        }
    }
    
    override fun getAllDownloads(): Flow<List<DownloadEntity>> {
        return downloadDao.getAllDownloads()
    }
    
    override fun getActiveDownloads(): Flow<List<DownloadEntity>> {
        return downloadDao.getActiveDownloads()
    }
    
    override fun getCompletedDownloads(): Flow<List<DownloadEntity>> {
        return downloadDao.getCompletedDownloads()
    }
    
    override fun getDownloadsByStatus(status: DownloadStatus): Flow<List<DownloadEntity>> {
        return downloadDao.getDownloadsByStatus(status)
    }
    
    override suspend fun updateDownloadPriority(trackId: String, priority: DownloadPriority): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                downloadDao.updateDownloadPriority(trackId, priority)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateDownloadId(trackId: String, downloadId: Long): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                downloadDao.updateDownloadId(trackId, downloadId)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateDownloadStatus(trackId: String, status: DownloadStatus, progress: Int): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                downloadDao.updateDownloadStatus(trackId, status, progress)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun reorderDownloads(downloadIds: List<String>): Result<Unit> {
        // Implementation for reordering downloads
        return Result.success(Unit)
    }
    
    override suspend fun clearFailedDownloads(): Result<Unit> {
        return try {
            val cutoffDate = LocalDateTime.now().minusHours(24)
            withContext(Dispatchers.IO) {
                downloadDao.deleteOldFailedDownloads(cutoffDate)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getTotalDownloadedSize(): Long {
        return withContext(Dispatchers.IO) {
            downloadDao.getTotalDownloadedSize().first() ?: 0L
        }
    }
    
    override suspend fun getCompletedDownloadsCount(): Int {
        return withContext(Dispatchers.IO) {
            downloadDao.getCompletedDownloadsCount().first()
        }
    }
    
    override suspend fun cleanupOldDownloads(): Result<Unit> {
        return try {
            clearFailedDownloads()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun searchDownloads(query: String): Flow<List<DownloadEntity>> {
        return downloadDao.searchDownloads(query)
    }
    
    override fun getDownloadsByArtist(artist: String): Flow<List<DownloadEntity>> {
        return downloadDao.searchDownloads(artist)
    }
    
    override fun getDownloadsByAlbum(album: String): Flow<List<DownloadEntity>> {
        return downloadDao.searchDownloads(album)
    }
    
    // Helper methods
    private fun getLocalFilePath(trackId: String): String {
        val musicDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        return File(musicDir, "$trackId.mp3").absolutePath
    }
    
    private fun getPriorityValue(priority: DownloadPriority): Int {
        return when (priority) {
            DownloadPriority.HIGH -> 1000
            DownloadPriority.NORMAL -> 0
            DownloadPriority.LOW -> -1000
        }
    }
}
