package com.example.mymusic.presentation.navigation

import androidx.compose.runtime.Composable
import com.example.mymusic.presentation.discovery.DiscoveryScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
        
        composable("explore") {
            DiscoveryScreen()
        }
        
        composable("downloads") {
            // TODO: Implement Downloads screen
            TrendingScreen() // Temporary placeholder
        }
        
        composable("player") {
            PlayerRoute(navController)
        }
    }
}
