package com.example.mymusic.domain.repository

import com.example.mymusic.domain.model.Track

interface TrackRepository {
    suspend fun getTrendingTracks(limit: Int, offset: Int): List<Track>
}


