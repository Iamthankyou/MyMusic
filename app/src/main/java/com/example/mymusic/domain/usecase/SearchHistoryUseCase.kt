package com.example.mymusic.domain.usecase

import com.example.mymusic.data.local.SearchHistoryDao
import com.example.mymusic.data.local.SearchHistoryEntity
import com.example.mymusic.domain.model.SearchFilter
import com.example.mymusic.domain.model.SearchType
import com.example.mymusic.domain.model.DurationFilter
import com.example.mymusic.domain.model.YearFilter
import com.example.mymusic.domain.model.PopularityFilter
import com.example.mymusic.domain.model.RatingFilter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchHistoryUseCase @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao
) {
    
    fun getRecentSearches(limit: Int = 100): Flow<List<SearchHistoryEntity>> {
        return searchHistoryDao.getRecentSearches(limit)
    }
    
    fun searchHistory(query: String): Flow<List<SearchHistoryEntity>> {
        return searchHistoryDao.searchHistory(query)
    }
    
    suspend fun saveSearch(query: String, filters: SearchFilter? = null) {
        val filtersJson = filters?.let { 
            // Simple JSON representation for now
            buildString {
                append("{")
                if (filters.searchType != SearchType.TRACK) append("\"type\":\"${filters.searchType}\",")
                if (filters.duration != DurationFilter.ANY) append("\"duration\":\"${filters.duration}\",")
                if (filters.year != YearFilter.ANY) append("\"year\":\"${filters.year}\",")
                if (filters.popularity != PopularityFilter.ANY) append("\"popularity\":\"${filters.popularity}\",")
                if (filters.rating != RatingFilter.ANY) append("\"rating\":\"${filters.rating}\",")
                if (filters.genres.isNotEmpty()) append("\"genres\":[${filters.genres.joinToString(",") { "\"$it\"" }}],")
                if (filters.tags.isNotEmpty()) append("\"tags\":[${filters.tags.joinToString(",") { "\"$it\"" }}],")
                if (endsWith(",")) deleteCharAt(length - 1)
                append("}")
            }
        }
        
        val searchEntity = SearchHistoryEntity(
            query = query,
            filters = filtersJson
        )
        searchHistoryDao.insertSearch(searchEntity)
    }
    
    suspend fun deleteSearch(search: SearchHistoryEntity) {
        searchHistoryDao.deleteSearch(search)
    }
    
    suspend fun deleteSearchById(id: Long) {
        searchHistoryDao.deleteSearchById(id)
    }
    
    suspend fun clearAllHistory() {
        searchHistoryDao.clearAllHistory()
    }
    
    suspend fun getHistoryCount(): Int {
        return searchHistoryDao.getHistoryCount()
    }
}
