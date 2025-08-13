package com.example.mymusic.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchFilter(
    val query: String = "",
    val album: String? = null,
    val tags: List<String> = emptyList(),
    val genres: List<String> = emptyList(),
    val durationMin: Int? = null, // in seconds
    val durationMax: Int? = null, // in seconds
    val yearMin: Int? = null,
    val yearMax: Int? = null,
    val popularityMin: Int? = null, // 0-100
    val popularityMax: Int? = null, // 0-100
    val ratingMin: Float? = null, // 0.0-5.0
    val ratingMax: Float? = null // 0.0-5.0
) {
    
    fun hasFilters(): Boolean {
        return album != null || 
               tags.isNotEmpty() || 
               genres.isNotEmpty() || 
               durationMin != null || 
               durationMax != null || 
               yearMin != null || 
               yearMax != null || 
               popularityMin != null || 
               popularityMax != null || 
               ratingMin != null || 
               ratingMax != null
    }
    
    fun toQueryParams(): Map<String, String> {
        val params = mutableMapOf<String, String>()
        
        if (album != null) params["album"] = album
        if (tags.isNotEmpty()) params["tags"] = tags.joinToString(",")
        if (genres.isNotEmpty()) params["genres"] = genres.joinToString(",")
        if (durationMin != null) params["duration_min"] = durationMin.toString()
        if (durationMax != null) params["duration_max"] = durationMax.toString()
        if (yearMin != null) params["year_min"] = yearMin.toString()
        if (yearMax != null) params["year_max"] = yearMax.toString()
        if (popularityMin != null) params["popularity_min"] = popularityMin.toString()
        if (popularityMax != null) params["popularity_max"] = popularityMax.toString()
        if (ratingMin != null) params["rating_min"] = ratingMin.toString()
        if (ratingMax != null) params["rating_max"] = ratingMax.toString()
        
        return params
    }
    
    companion object {
        fun empty() = SearchFilter()
    }
}
