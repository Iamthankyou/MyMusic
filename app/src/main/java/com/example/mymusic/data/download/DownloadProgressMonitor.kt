package com.example.mymusic.data.download

import android.app.DownloadManager
import android.content.Context
import android.util.Log
import com.example.mymusic.data.local.DownloadStatus
import com.example.mymusic.domain.repository.DownloadRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

class DownloadProgressMonitor(
    private val context: Context,
    private val downloadRepository: DownloadRepository
) {
    
    companion object {
        private const val TAG = "DownloadProgressMonitor"
        private const val PROGRESS_UPDATE_INTERVAL = 1000L // 1 second
    }
    
    private val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    private val monitorJob = Job()
    private val monitorScope = CoroutineScope(Dispatchers.IO + monitorJob)
    
    fun startMonitoring() {
        monitorScope.launch {
            while (isActive) {
                try {
                    updateDownloadProgress()
                    delay(PROGRESS_UPDATE_INTERVAL)
                } catch (e: Exception) {
                    Log.e(TAG, "Error monitoring download progress", e)
                    delay(PROGRESS_UPDATE_INTERVAL)
                }
            }
        }
    }
    
    private suspend fun updateDownloadProgress() {
        val downloads = downloadRepository.getAllDownloads().first()
        val activeDownloads = downloads.filter { 
            it.status == DownloadStatus.DOWNLOADING && it.downloadId != null 
        }
        
        for (download in activeDownloads) {
            val downloadId = download.downloadId ?: continue
            
            val query = DownloadManager.Query().setFilterById(downloadId)
            val cursor = downloadManager.query(query)
            
            if (cursor.moveToFirst()) {
                val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val status = cursor.getInt(statusIndex)
                
                when (status) {
                    DownloadManager.STATUS_RUNNING -> {
                        val bytesDownloadedIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                        val totalSizeIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                        
                        val bytesDownloaded = cursor.getLong(bytesDownloadedIndex)
                        val totalSize = cursor.getLong(totalSizeIndex)
                        
                        if (totalSize > 0) {
                            val progress = ((bytesDownloaded * 100) / totalSize).toInt()
                            downloadRepository.updateDownloadStatus(download.trackId, DownloadStatus.DOWNLOADING, progress)
                            Log.d(TAG, "Progress update for ${download.title}: $progress%")
                        }
                    }
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        downloadRepository.updateDownloadStatus(download.trackId, DownloadStatus.COMPLETED, 100)
                        Log.d(TAG, "Download completed for ${download.title}")
                    }
                    DownloadManager.STATUS_FAILED -> {
                        downloadRepository.updateDownloadStatus(download.trackId, DownloadStatus.FAILED, 0)
                        Log.e(TAG, "Download failed for ${download.title}")
                    }
                    DownloadManager.STATUS_PAUSED -> {
                        // Keep current progress but mark as paused if needed
                        Log.d(TAG, "Download paused for ${download.title}")
                    }
                }
            }
            cursor.close()
        }
    }
    
    fun stopMonitoring() {
        monitorJob.cancel()
    }
}
