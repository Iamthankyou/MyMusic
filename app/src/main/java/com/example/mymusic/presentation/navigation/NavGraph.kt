package com.example.mymusic.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mymusic.presentation.detail.DetailViewModel
import com.example.mymusic.presentation.detail.TrackDetailScreen
import com.example.mymusic.presentation.discovery.DiscoveryScreen
import com.example.mymusic.presentation.home.trending.TrendingScreen
import com.example.mymusic.presentation.search.SearchScreen
import com.example.mymusic.presentation.player.PlayerRoute

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
            TrendingScreen()
        }
        
        composable("search") {
            SearchScreen()
        }
        
        composable("discovery") {
            DiscoveryScreen(
                onTrackClicked = { track ->
                    // Handle track click - navigate to player or start playback
                }
            )
        }
        
        composable("downloads") {
            // TODO: Implement Downloads screen
            TrendingScreen() // Temporary placeholder
        }
        
        composable("player") {
            PlayerRoute(navController)
        }
        
        composable(
            route = "track_detail/{trackId}/{trackTitle}/{trackArtist}/{trackArtworkUrl}/{trackAudioUrl}/{trackDurationMs}",
            arguments = listOf(
                androidx.navigation.navArgument("trackId") { type = androidx.navigation.NavType.StringType },
                androidx.navigation.navArgument("trackTitle") { type = androidx.navigation.NavType.StringType },
                androidx.navigation.navArgument("trackArtist") { type = androidx.navigation.NavType.StringType },
                androidx.navigation.navArgument("trackArtworkUrl") { type = androidx.navigation.NavType.StringType },
                androidx.navigation.navArgument("trackAudioUrl") { type = androidx.navigation.NavType.StringType },
                androidx.navigation.navArgument("trackDurationMs") { type = androidx.navigation.NavType.LongType }
            )
        ) { backStackEntry ->
            val viewModel: DetailViewModel = androidx.hilt.navigation.compose.hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            
            uiState.track?.let { track ->
                TrackDetailScreen(
                    track = track,
                    relatedTracks = uiState.relatedTracks,
                    onTrackClicked = { relatedTrack ->
                        viewModel.onRelatedTrackClicked(relatedTrack)
                    },
                    onBackPressed = { navController.popBackStack() },
                    onPlayClicked = { viewModel.onPlayClicked() },
                    onShareClicked = { viewModel.onShareClicked() }
                )
            }
        }
    }
}
