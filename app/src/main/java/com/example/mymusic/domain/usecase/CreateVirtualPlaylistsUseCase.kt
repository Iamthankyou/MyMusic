package com.example.mymusic.domain.usecase

import android.util.Log
import com.example.mymusic.domain.model.Playlist
import com.example.mymusic.domain.model.Track
import com.example.mymusic.domain.usecase.GetTrendingTracksUseCase
import javax.inject.Inject

class CreateVirtualPlaylistsUseCase @Inject constructor(
    private val getTrendingTracksUseCase: GetTrendingTracksUseCase
) {
    suspend operator fun invoke(): List<Playlist> {
        try {
            Log.d("CreateVirtualPlaylistsUseCase", "Fetching trending tracks...")
            val allTracks = getTrendingTracksUseCase(limit = 100, offset = 0)
            Log.d("CreateVirtualPlaylistsUseCase", "Fetched ${allTracks.size} tracks")
            
            if (allTracks.isEmpty()) {
                Log.w("CreateVirtualPlaylistsUseCase", "No tracks available!")
                return emptyList()
            }
            
            // Tạo 5 playlists với themes khác nhau
            val playlists = listOf(
                Playlist(
                    id = "jamendo_virtual_popular",
                    title = "🔥 Popular Hits",
                    description = "Most popular trending tracks",
                    artworkUrl = allTracks.firstOrNull()?.artworkUrl,
                    trackCount = minOf(20, allTracks.size),
                    creator = "MyMusic",
                    tracks = allTracks.take(20)
                ),
                Playlist(
                    id = "jamendo_virtual_latest",
                    title = "⭐ Latest Releases", 
                    description = "Fresh new music releases",
                    artworkUrl = allTracks.drop(20).firstOrNull()?.artworkUrl,
                    trackCount = minOf(20, allTracks.drop(20).size),
                    creator = "MyMusic",
                    tracks = allTracks.drop(20).take(20)
                ),
                Playlist(
                    id = "jamendo_virtual_mix",
                    title = "🎵 Mixed Vibes",
                    description = "Eclectic mix of trending tracks",
                    artworkUrl = allTracks.drop(40).firstOrNull()?.artworkUrl,
                    trackCount = minOf(20, allTracks.drop(40).size),
                    creator = "MyMusic",
                    tracks = allTracks.drop(40).take(20)
                ),
                Playlist(
                    id = "jamendo_virtual_chill",
                    title = "😌 Chill Vibes",
                    description = "Relaxing and chill music",
                    artworkUrl = allTracks.drop(60).firstOrNull()?.artworkUrl,
                    trackCount = minOf(20, allTracks.drop(60).size),
                    creator = "MyMusic",
                    tracks = allTracks.drop(60).take(20)
                ),
                Playlist(
                    id = "jamendo_virtual_energy",
                    title = "⚡ High Energy",
                    description = "Upbeat and energetic tracks",
                    artworkUrl = allTracks.drop(80).firstOrNull()?.artworkUrl,
                    trackCount = minOf(20, allTracks.drop(80).size),
                    creator = "MyMusic",
                    tracks = allTracks.drop(80).take(20)
                )
            )
            
            Log.d("CreateVirtualPlaylistsUseCase", "Created ${playlists.size} playlists with total ${playlists.sumOf { it.tracks.size }} tracks")
            return playlists
            
        } catch (e: Exception) {
            Log.e("CreateVirtualPlaylistsUseCase", "Error creating virtual playlists", e)
            return emptyList()
        }
    }
}
