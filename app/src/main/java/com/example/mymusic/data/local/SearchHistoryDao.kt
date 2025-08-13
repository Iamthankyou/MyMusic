package com.example.mymusic.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    
    @Query("SELECT * FROM search_history ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentSearches(limit: Int = 100): Flow<List<SearchHistoryEntity>>
    
    @Query("SELECT * FROM search_history WHERE query LIKE '%' || :query || '%' ORDER BY timestamp DESC LIMIT :limit")
    fun searchHistory(query: String, limit: Int = 20): Flow<List<SearchHistoryEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSearch(search: SearchHistoryEntity): Long
    
    @Delete
    fun deleteSearch(search: SearchHistoryEntity): Int
    
    @Query("DELETE FROM search_history WHERE id = :id")
    fun deleteSearchById(id: Long): Int
    
    @Query("DELETE FROM search_history")
    fun clearAllHistory(): Int
    
    @Query("SELECT COUNT(*) FROM search_history")
    fun getHistoryCount(): Int
}
