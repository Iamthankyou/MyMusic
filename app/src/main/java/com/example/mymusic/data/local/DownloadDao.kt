package com.example.mymusic.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface DownloadDao {
    
    @Query("SELECT * FROM downloads WHERE status != 'DELETED' ORDER BY priority DESC, downloadDate ASC")
    fun getAllDownloads(): Flow<List<DownloadEntity>>
    
    @Query("SELECT * FROM downloads WHERE status = :status ORDER BY priority DESC, downloadDate ASC")
    fun getDownloadsByStatus(status: DownloadStatus): Flow<List<DownloadEntity>>
    
    @Query("SELECT * FROM downloads WHERE status IN ('PENDING', 'DOWNLOADING') ORDER BY priority DESC, downloadDate ASC")
    fun getActiveDownloads(): Flow<List<DownloadEntity>>
    
    @Query("SELECT * FROM downloads WHERE status = 'COMPLETED' ORDER BY downloadDate DESC")
    fun getCompletedDownloads(): Flow<List<DownloadEntity>>
    
    @Query("SELECT * FROM downloads WHERE trackId = :trackId")
    fun getDownloadByTrackId(trackId: String): Flow<DownloadEntity?>
    
    @Query("SELECT COUNT(*) FROM downloads WHERE status = 'COMPLETED'")
    fun getCompletedDownloadsCount(): Flow<Int>
    
    @Query("SELECT SUM(fileSize) FROM downloads WHERE status = 'COMPLETED'")
    fun getTotalDownloadedSize(): Flow<Long?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDownload(download: DownloadEntity)
    
    @Update
    fun updateDownload(download: DownloadEntity)
    
    @Query("UPDATE downloads SET status = :status, progress = :progress WHERE trackId = :trackId")
    fun updateDownloadStatus(trackId: String, status: DownloadStatus, progress: Int)
    
    @Query("UPDATE downloads SET downloadId = :downloadId WHERE trackId = :trackId")
    fun updateDownloadId(trackId: String, downloadId: Long)
    
    @Query("UPDATE downloads SET priority = :priority WHERE trackId = :trackId")
    fun updateDownloadPriority(trackId: String, priority: DownloadPriority)
    
    @Query("DELETE FROM downloads WHERE trackId = :trackId")
    fun deleteDownload(trackId: String)
    
    @Query("DELETE FROM downloads WHERE status = 'FAILED' AND downloadDate < :cutoffDate")
    fun deleteOldFailedDownloads(cutoffDate: LocalDateTime)
    
    @Query("SELECT * FROM downloads WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%'")
    fun searchDownloads(query: String): Flow<List<DownloadEntity>>
}
