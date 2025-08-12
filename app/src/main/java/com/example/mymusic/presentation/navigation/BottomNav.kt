package com.example.mymusic.presentation.navigation

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class NavItem(val route: String, val label: String) {
    data object Trending : NavItem("trending", "Trending")
    data object Explore : NavItem("explore", "Explore")
    data object Downloads : NavItem("downloads", "Downloads")
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(NavItem.Trending, NavItem.Explore, NavItem.Downloads)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { item ->
            val selected = currentDestination.isTopLevelDestination(item.route)
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { 
                    Text(
                        text = getIconForRoute(item.route),
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium
                    ) 
                },
                label = { Text(item.label) }
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestination(route: String): Boolean {
    return this?.hierarchy?.any { it.route == route } == true
}

private fun getIconForRoute(route: String): String {
    return when (route) {
        "trending" -> "🔥" // Trending/hot icon
        "explore" -> "🔍" // Search/explore icon
        "downloads" -> "⬇" // Download icon
        else -> "•"
    }
}


