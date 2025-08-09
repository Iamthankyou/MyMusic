package com.example.mymusic.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mymusic.data.mapper.TrackMapper
import com.example.mymusic.data.remote.JamendoTracksService
import com.example.mymusic.domain.model.Track

class TrendingTracksPagingSource(
    private val service: JamendoTracksService,
    private val pageSize: Int
) : PagingSource<Int, Track>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Track> {
        return try {
            val offset = params.key ?: 0
            val response = service.getTrendingTracks(limit = pageSize, offset = offset)
            val items = response.results.map { TrackMapper.fromDto(it) }
            val nextKey = if (items.size < pageSize) null else offset + pageSize
            LoadResult.Page(
                data = items,
                prevKey = if (offset == 0) null else (offset - pageSize).coerceAtLeast(0),
                nextKey = nextKey
            )
        } catch (t: Throwable) {
            LoadResult.Error(t)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Track>): Int? {
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor) ?: return null
        return page.prevKey?.plus(pageSize) ?: page.nextKey?.minus(pageSize)
    }
}


