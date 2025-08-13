# Tại Sao Cần Dùng Room Database cho Story 3.2?

**Story 3.2:** Advanced Search Implementation với Local Storage  
**Ngày:** 14/08/2025  
**Tác giả:** BMad Orchestrator  

---

## 🎯 Yêu Cầu của Story 3.2

Story 3.2 cần implement **advanced search functionality** với các tính năng:
- Lưu trữ search history
- Advanced filters (album, tags, genres, duration, year)
- Tìm kiếm trong search history
- Quản lý và xóa search history

---

## 🤔 Tại Sao Không Dùng SharedPreferences hoặc DataStore?

### SharedPreferences
```kotlin
// ❌ Không thể làm được:
// - Complex queries (tìm kiếm theo nhiều tiêu chí)
// - Structured data storage
// - Search history với filters
// - Pagination và sorting
```

### DataStore
```kotlin
// ❌ Hạn chế:
// - Chỉ lưu key-value pairs
// - Không hỗ trợ SQL queries
// - Không thể tìm kiếm phức tạp
// - Không có schema migration
```

---

## ✅ Tại Sao Chọn Room Database?

### 1. **Complex Queries** - Yêu cầu chính của Story 3.2

```kotlin
// ✅ Room có thể làm được:
@Query("""
    SELECT * FROM search_history 
    WHERE query LIKE '%' || :searchTerm || '%'
    AND (album = :album OR :album IS NULL)
    AND (duration BETWEEN :minDuration AND :maxDuration)
    ORDER BY timestamp DESC
    LIMIT :limit
""")
fun searchHistoryWithFilters(
    searchTerm: String,
    album: String?,
    minDuration: Int,
    maxDuration: Int,
    limit: Int
): Flow<List<SearchHistoryEntity>>
```

### 2. **Structured Data Storage** - Search History với Filters

```kotlin
// ✅ Room Entity với structured data:
@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val query: String,
    val timestamp: Long = System.currentTimeMillis(),
    val album: String? = null,
    val tags: String? = null,  // JSON string
    val genres: String? = null, // JSON string
    val durationMin: Int? = null,
    val durationMax: Int? = null,
    val yearMin: Int? = null,
    val yearMax: Int? = null
)
```

### 3. **Advanced Search Filters** - Yêu cầu của Story 3.2

```kotlin
// ✅ Room có thể handle complex filtering:
data class SearchFilter(
    val query: String,
    val album: String? = null,
    val tags: List<String> = emptyList(),
    val genres: List<String> = emptyList(),
    val durationMin: Int? = null,
    val durationMax: Int? = null,
    val yearMin: Int? = null,
    val yearMax: Int? = null
)
```

### 4. **Search History Management** - CRUD Operations

```kotlin
// ✅ Room DAO với đầy đủ operations:
@Dao
interface SearchHistoryDao {
    // Lấy search history với pagination
    @Query("SELECT * FROM search_history ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentSearches(limit: Int = 100): Flow<List<SearchHistoryEntity>>
    
    // Tìm kiếm trong history
    @Query("SELECT * FROM search_history WHERE query LIKE '%' || :query || '%'")
    fun searchHistory(query: String): Flow<List<SearchHistoryEntity>>
    
    // Lưu search mới
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSearch(search: SearchHistoryEntity): Long
    
    // Xóa search history
    @Query("DELETE FROM search_history WHERE id = :id")
    fun deleteSearchById(id: Long): Int
    
    // Xóa tất cả history
    @Query("DELETE FROM search_history")
    fun clearAllHistory(): Int
}
```

---

## 📊 So Sánh Các Giải Pháp

| Tính Năng | SharedPreferences | DataStore | **Room Database** |
|-----------|-------------------|-----------|-------------------|
| **Complex Queries** | ❌ Không | ❌ Không | ✅ **SQL Support** |
| **Search History** | ⚠️ Hạn chế | ⚠️ Basic | ✅ **Full Support** |
| **Advanced Filters** | ❌ Không | ❌ Không | ✅ **Complex Filtering** |
| **Type Safety** | ❌ Runtime | ⚠️ Basic | ✅ **Compile-time** |
| **Schema Migration** | ❌ Không | ❌ Không | ✅ **Migration Support** |
| **Performance** | ✅ Fast | ✅ Fast | ✅ **Optimized Queries** |
| **Learning Value** | ⚠️ Limited | ⚠️ Limited | ✅ **Industry Standard** |

---

## 🎯 Kết Luận

**Room Database là LỰA CHỌN DUY NHẤT** cho Story 3.2 vì:

1. **✅ Complex Queries** - Cần thiết cho advanced search filters
2. **✅ Structured Data** - Search history với multiple fields
3. **✅ Type Safety** - Compile-time safety cho data operations
4. **✅ Industry Standard** - Best practice cho Android development
5. **✅ Future Scalability** - Dễ dàng mở rộng tính năng

**Các giải pháp khác (SharedPreferences, DataStore) không thể đáp ứng được yêu cầu complex queries và advanced filtering của Story 3.2.**

---

## 🔗 References

- [Room Database Guide](https://developer.android.com/training/data-storage/room)
- [Room vs DataStore](https://developer.android.com/topic/libraries/architecture/datastore)
- [Android Storage Options](https://developer.android.com/training/data-storage)

---

**Tài liệu này giải thích rõ lý do kỹ thuật tại sao Room Database là lựa chọn bắt buộc cho Story 3.2.**
