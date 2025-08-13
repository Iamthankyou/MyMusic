package com.example.mymusic.domain.usecase

import com.example.mymusic.data.local.SearchHistoryDao
import com.example.mymusic.data.local.SearchHistoryEntity
import com.example.mymusic.domain.model.SearchFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchHistoryUseCase @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao
) {
    
    fun getRecentSearches(limit: Int = 100): Flow<List<SearchHistoryEntity>> {
        return searchHistoryDao.getRecentSearches(limit)
    }
    
    fun searchHistory(query: String, limit: Int = 20): Flow<List<SearchHistoryEntity>> {
        return searchHistoryDao.searchHistory(query, limit)
    }
    
    suspend fun saveSearch(query: String, filters: SearchFilter? = null, resultCount: Int = 0) {
        withContext(Dispatchers.IO) {
            val filtersJson = filters?.let { 
                // In a real app, you'd use proper JSON serialization
                buildString {
                    if (it.album != null) append("album:${it.album},")
                    if (it.tags.isNotEmpty()) append("tags:${it.tags.joinToString(",")},")
                    if (it.genres.isNotEmpty()) append("genres:${it.genres.joinToString(",")},")
                    if (it.durationMin != null) append("durationMin:${it.durationMin},")
                    if (it.durationMax != null) append("durationMax:${it.durationMax},")
                    if (it.yearMin != null) append("yearMin:${it.yearMin},")
                    if (it.yearMax != null) append("yearMax:${it.yearMax},")
                    if (it.popularityMin != null) append("popularityMin:${it.popularityMin},")
                    if (it.popularityMax != null) append("popularityMax:${it.popularityMax},")
                    if (it.ratingMin != null) append("ratingMin:${it.ratingMin},")
                    if (it.ratingMax != null) append("ratingMax:${it.ratingMax},")
                }.removeSuffix(",")
            }
            
            val searchEntity = SearchHistoryEntity(
                query = query,
                filters = filtersJson,
                resultCount = resultCount
            )
            
            searchHistoryDao.insertSearch(searchEntity)
        }
    }
    
    suspend fun deleteSearch(search: SearchHistoryEntity) {
        withContext(Dispatchers.IO) {
            searchHistoryDao.deleteSearch(search)
        }
    }
    
    suspend fun deleteSearchById(id: Long) {
        withContext(Dispatchers.IO) {
            searchHistoryDao.deleteSearchById(id)
        }
    }
    
    suspend fun clearAllHistory() {
        withContext(Dispatchers.IO) {
            searchHistoryDao.clearAllHistory()
        }
    }
    
    suspend fun getHistoryCount(): Int {
        return withContext(Dispatchers.IO) {
            searchHistoryDao.getHistoryCount()
        }
    }
}
