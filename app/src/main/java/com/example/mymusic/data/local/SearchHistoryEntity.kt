package com.example.mymusic.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val query: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val filters: String? = null, // JSON string of applied filters
    val resultCount: Int = 0
)
