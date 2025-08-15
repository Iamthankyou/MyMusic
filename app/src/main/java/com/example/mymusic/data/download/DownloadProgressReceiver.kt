package com.example.mymusic.data.download

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.mymusic.data.local.DownloadStatus
import com.example.mymusic.domain.repository.DownloadRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class DownloadProgressReceiver : BroadcastReceiver() {
    
    companion object {
        private const val TAG = "DownloadProgressReceiver"
    }
    
    @Inject
    lateinit var downloadRepository: DownloadRepository
    
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
            val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            
            if (downloadId != -1L) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // Find the track ID for this download ID
                        val downloads = downloadRepository.getAllDownloads().first()
                        val download = downloads.find { download -> download.downloadId == downloadId }
                        
                        if (download != null) {
                            // Check if download was successful
                            val downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager
                            val query = DownloadManager.Query().setFilterById(downloadId)
                            val cursor = downloadManager?.query(query)
                            
                            if (cursor?.moveToFirst() == true) {
                                val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                                val status = cursor.getInt(columnIndex)
                                
                                when (status) {
                                    DownloadManager.STATUS_SUCCESSFUL -> {
                                        // Get file size
                                        val sizeIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                                        val fileSize = cursor.getLong(sizeIndex)
                                        
                                        downloadRepository.updateDownloadStatus(
                                            download.trackId, 
                                            DownloadStatus.COMPLETED, 
                                            100
                                        )
                                        
                                        // Update file size in database
                                        try {
                                            val localFile = java.io.File(download.localFilePath)
                                            if (localFile.exists()) {
                                                val actualFileSize = localFile.length()
                                                // TODO: Add method to update file size in DownloadRepository
                                                Log.d(TAG, "Download completed for track: ${download.title}, size: $actualFileSize bytes")
                                            }
                                        } catch (e: Exception) {
                                            Log.e(TAG, "Error getting file size for ${download.title}", e)
                                        }
                                        
                                        Log.d(TAG, "Download completed for track: ${download.title}")
                                    }
                                    DownloadManager.STATUS_FAILED -> {
                                        downloadRepository.updateDownloadStatus(
                                            download.trackId, 
                                            DownloadStatus.FAILED, 
                                            0
                                        )
                                        Log.e(TAG, "Download failed for track: ${download.title}")
                                    }
                                }
                            }
                            cursor?.close()
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error processing download completion", e)
                    }
                }
            }
        }
    }
}
