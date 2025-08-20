package com.example.mymusic.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.mymusic.presentation.discovery.DiscoveryScreen
import com.example.mymusic.presentation.home.trending.TrendingScreen
import com.example.mymusic.presentation.search.SearchScreen
import com.example.mymusic.presentation.player.PlayerRoute
import com.example.mymusic.presentation.detail.TrackDetailScreen
import com.example.mymusic.presentation.download.DownloadsScreen
import com.example.mymusic.presentation.playlist.PlaylistListScreen
import com.example.mymusic.presentation.playlist.PlaylistDetailScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "trending",
        modifier = modifier
    ) {
        composable("trending") {
            TrendingScreen(navController = navController)
        }
        
        composable("search") {
            SearchScreen(navController = navController)
        }
        composable(
            route = "search?query={query}",
            arguments = listOf(
                navArgument("query") { type = NavType.StringType; nullable = true; defaultValue = null }
            )
        ) {
            SearchScreen(navController = navController)
        }
        
        composable("explore") {
            DiscoveryScreen(navController = navController)
        }
        
        composable("playlists") {
            PlaylistListScreen(navController = navController)
        }
        
        composable("downloads") {
            DownloadsScreen(
                onNavigateToPlayer = { trackId ->
                    navController.navigate("player")
                }
            )
        }
        
        composable("player") {
            PlayerRoute(navController)
        }
        
        // Detail routes for Story 3.4
        composable("track_detail/{trackId}") { backStackEntry ->
            val trackId = backStackEntry.arguments?.getString("trackId") ?: ""
            TrackDetailScreen(
                trackId = trackId,
                navController = navController
            )
        }
        
        // Playlist detail route
        composable("playlist_detail/{playlistId}") { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getString("playlistId") ?: ""
            PlaylistDetailScreen(
                playlistId = playlistId,
                navController = navController
            )
        }
    }
}
