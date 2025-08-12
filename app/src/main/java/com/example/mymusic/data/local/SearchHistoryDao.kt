package com.example.mymusic.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    
    @Query("SELECT * FROM search_history ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentSearches(limit: Int = 100): Flow<List<SearchHistoryEntity>>
    
    @Query("SELECT * FROM search_history WHERE query LIKE '%' || :query || '%' ORDER BY timestamp DESC LIMIT 10")
    fun searchHistory(query: String): Flow<List<SearchHistoryEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(search: SearchHistoryEntity)
    
    @Delete
    suspend fun deleteSearch(search: SearchHistoryEntity)
    
    @Query("DELETE FROM search_history WHERE id = :id")
    suspend fun deleteSearchById(id: Long)
    
    @Query("DELETE FROM search_history")
    suspend fun clearAllHistory()
    
    @Query("SELECT COUNT(*) FROM search_history")
    suspend fun getHistoryCount(): Int
}
