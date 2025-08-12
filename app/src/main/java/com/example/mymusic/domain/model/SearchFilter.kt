package com.example.mymusic.domain.model

data class SearchFilter(
    val query: String = "",
    val searchType: SearchType = SearchType.TRACK,
    val duration: DurationFilter = DurationFilter.ANY,
    val year: YearFilter = YearFilter.ANY,
    val popularity: PopularityFilter = PopularityFilter.ANY,
    val rating: RatingFilter = RatingFilter.ANY,
    val genres: List<String> = emptyList(),
    val tags: List<String> = emptyList()
) {
    fun hasActiveFilters(): Boolean {
        return searchType != SearchType.TRACK ||
                duration != DurationFilter.ANY ||
                year != YearFilter.ANY ||
                popularity != PopularityFilter.ANY ||
                rating != RatingFilter.ANY ||
                genres.isNotEmpty() ||
                tags.isNotEmpty()
    }
    
    fun clearFilters(): SearchFilter {
        return copy(
            searchType = SearchType.TRACK,
            duration = DurationFilter.ANY,
            year = YearFilter.ANY,
            popularity = PopularityFilter.ANY,
            rating = RatingFilter.ANY,
            genres = emptyList(),
            tags = emptyList()
        )
    }
}

enum class SearchType {
    TRACK, ALBUM, ARTIST, TAG, GENRE
}

enum class DurationFilter(val label: String, val minSeconds: Int? = null, val maxSeconds: Int? = null) {
    ANY("Any"),
    SHORT("Short (< 3 min)", maxSeconds = 180),
    MEDIUM("Medium (3-5 min)", minSeconds = 180, maxSeconds = 300),
    LONG("Long (> 5 min)", minSeconds = 300)
}

enum class YearFilter(val label: String, val minYear: Int? = null, val maxYear: Int? = null) {
    ANY("Any"),
    RECENT("Recent (2020+)", minYear = 2020),
    MODERN("Modern (2010-2019)", minYear = 2010, maxYear = 2019),
    CLASSIC("Classic (2000-2009)", minYear = 2000, maxYear = 2009),
    OLD("Old (< 2000)", maxYear = 1999)
}

enum class PopularityFilter(val label: String, val minPopularity: Int? = null) {
    ANY("Any"),
    HIGH("High (> 80)", minPopularity = 80),
    MEDIUM("Medium (50-80)", minPopularity = 50),
    LOW("Low (< 50)", minPopularity = 0)
}

enum class RatingFilter(val label: String, val minRating: Float? = null) {
    ANY("Any"),
    EXCELLENT("Excellent (4.5+)", minRating = 4.5f),
    GOOD("Good (4.0+)", minRating = 4.0f),
    AVERAGE("Average (3.0+)", minRating = 3.0f)
}
