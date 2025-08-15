package com.example.mymusic.data.download

import android.app.DownloadManager
import android.app.IntentService
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.example.mymusic.data.local.DownloadEntity
import com.example.mymusic.data.local.DownloadStatus
import com.example.mymusic.domain.repository.DownloadRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DownloadService : IntentService("DownloadService") {
    
    companion object {
        private const val TAG = "DownloadService"
        const val EXTRA_PRIORITY = "extra_priority"
    }
    
    @Inject
    lateinit var downloadRepository: DownloadRepository
    
    private lateinit var downloadManager: DownloadManager
    
    override fun onCreate() {
        super.onCreate()
        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
    }
    
    override fun onHandleIntent(intent: Intent?) {
        // Since Track is not Parcelable, we'll need to pass individual fields
        val trackId = intent?.getStringExtra("track_id")
        val title = intent?.getStringExtra("track_title")
        val artist = intent?.getStringExtra("track_artist")
        val audioUrl = intent?.getStringExtra("track_audio_url")
        val priority = intent?.getIntExtra(EXTRA_PRIORITY, 1) ?: 1
        
        if (trackId != null && title != null && artist != null && audioUrl != null && audioUrl.isNotEmpty()) {
            startDownload(trackId, title, artist, audioUrl, priority)
        } else {
            Log.e(TAG, "Missing or invalid track information in DownloadService")
        }
    }
    
    private fun startDownload(trackId: String, title: String, artist: String, audioUrl: String, priority: Int) {
        try {
            // Create download request
            val request = DownloadManager.Request(Uri.parse(audioUrl))
                .setTitle(title)
                .setDescription("Downloading $artist")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalFilesDir(
                    this,
                    Environment.DIRECTORY_MUSIC,
                    "$trackId.mp3"
                )
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
            
            // Enqueue download
            val downloadId = downloadManager.enqueue(request)
            
            // Update database with download ID
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    downloadRepository.updateDownloadId(trackId, downloadId)
                    Log.d(TAG, "Download started for track: $title, ID: $downloadId")
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to update download ID in database", e)
                }
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start download for track: $title", e)
            
            // Update database with failed status
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    downloadRepository.updateDownloadStatus(trackId, DownloadStatus.FAILED, 0)
                } catch (dbException: Exception) {
                    Log.e(TAG, "Failed to update database with failed status", dbException)
                }
            }
        }
    }
}
