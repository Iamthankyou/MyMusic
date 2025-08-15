package com.example.mymusic.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "downloads")
data class DownloadEntity(
    @PrimaryKey val trackId: String,
    val title: String,
    val artist: String,
    val album: String?,
    val duration: Long,
    val imageUrl: String?,
    val audioUrl: String,
    val localFilePath: String,
    val fileSize: Long,
    val downloadDate: LocalDateTime = LocalDateTime.now(),
    val status: DownloadStatus = DownloadStatus.PENDING,
    val progress: Int = 0,
    val downloadId: Long? = null,
    val priority: DownloadPriority = DownloadPriority.NORMAL
)

enum class DownloadStatus {
    PENDING,      // Queued for download
    DOWNLOADING,  // Currently downloading
    COMPLETED,    // Successfully downloaded
    FAILED,       // Download failed
    DELETED       // User deleted download
}

enum class DownloadPriority {
    HIGH,
    NORMAL,
    LOW
}
