package com.example.mymusic.presentation.home.trending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mymusic.data.paging.TrendingTracksPagingSource
import com.example.mymusic.data.remote.JamendoTracksService
import com.example.mymusic.domain.model.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TrendingViewModel @Inject constructor(
    private val service: JamendoTracksService
) : ViewModel() {

    val pagingData: Flow<PagingData<Track>> = Pager(
        config = PagingConfig(pageSize = 20, prefetchDistance = 2),
        pagingSourceFactory = { TrendingTracksPagingSource(service, 20) }
    ).flow.cachedIn(viewModelScope)
}


