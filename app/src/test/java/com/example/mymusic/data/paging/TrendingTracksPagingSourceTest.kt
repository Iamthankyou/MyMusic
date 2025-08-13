package com.example.mymusic.data.paging

import androidx.paging.PagingSource
import com.example.mymusic.data.remote.JamendoResponse
import com.example.mymusic.data.remote.JamendoTracksService
import com.example.mymusic.data.remote.TrackDto
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class TrendingTracksPagingSourceTest {

    private class FakeService(private val items: List<TrackDto>) : JamendoTracksService {
        override suspend fun getTrendingTracks(limit: Int, offset: Int, order: String): JamendoResponse<TrackDto> {
            val slice = items.drop(offset).take(limit)
            return JamendoResponse(results = slice)
        }
        
        override suspend fun searchTracks(query: String, limit: Int, offset: Int, order: String): JamendoResponse<TrackDto> {
            val slice = items.drop(offset).take(limit)
            return JamendoResponse(results = slice)
        }
    }

    @Test
    fun load_firstPage_hasNextKey() = runBlocking {
        val list = (1..40).map { i ->
            TrackDto(id = i.toString(), name = "T$i", artistName = "A$i", durationSec = 120, audioUrl = null, imageUrl = null)
        }
        val pagingSource = TrendingTracksPagingSource(FakeService(list), pageSize = 20)
        val result = pagingSource.load(PagingSource.LoadParams.Refresh(key = null, loadSize = 20, placeholdersEnabled = false))
        val page = result as PagingSource.LoadResult.Page
        assertEquals(20, page.data.size)
        assertEquals(null, page.prevKey)
        assertEquals(20, page.nextKey)
    }

    @Test
    fun load_lastPage_noNextKey() = runBlocking {
        val list = (1..25).map { i ->
            TrackDto(id = i.toString(), name = "T$i", artistName = "A$i", durationSec = 120, audioUrl = null, imageUrl = null)
        }
        val pagingSource = TrendingTracksPagingSource(FakeService(list), pageSize = 20)
        // Load second page (offset 20) expecting <=5 items
        val result = pagingSource.load(PagingSource.LoadParams.Append(key = 20, loadSize = 20, placeholdersEnabled = false))
        val page = result as PagingSource.LoadResult.Page
        assertEquals(5, page.data.size)
        assertEquals(0, page.prevKey)
        assertEquals(null, page.nextKey)
    }
}


