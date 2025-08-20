package com.example.mymusic.presentation.navigation

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.automirrored.outlined.PlaylistPlay
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class NavItem(
    val route: String, 
    val label: String, 
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    data object Trending : NavItem(
        "trending", 
        "Home", 
        Icons.Outlined.Home,
        Icons.Filled.Home
    )
    data object Search : NavItem(
        "search", 
        "Search", 
        Icons.Outlined.Search,
        Icons.Filled.Search
    )
    data object Explore : NavItem(
        "explore", 
        "Browse", 
        Icons.Outlined.Explore,
        Icons.Filled.Explore
    )
    data object Playlists : NavItem(
        "playlists", 
        "Library", 
        Icons.AutoMirrored.Outlined.PlaylistPlay,
        Icons.AutoMirrored.Filled.PlaylistPlay
    )
    data object Downloads : NavItem(
        "downloads", 
        "Downloads", 
        Icons.Outlined.Download,
        Icons.Filled.Download
    )
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        NavItem.Trending, 
        NavItem.Search, 
        NavItem.Explore, 
        NavItem.Playlists, 
        NavItem.Downloads
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        contentColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .border(
                width = 0.5.dp,
                color = androidx.compose.material3.MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
    ) {
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
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.icon,
                        contentDescription = item.label,
                        tint = if (selected) {
                            androidx.compose.material3.MaterialTheme.colorScheme.primary
                        } else {
                            androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { 
                    Text(
                        text = item.label,
                        style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                        color = if (selected) {
                            androidx.compose.material3.MaterialTheme.colorScheme.primary
                        } else {
                            androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    ) 
                }
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestination(route: String): Boolean {
    return this?.hierarchy?.any { it.route == route } == true
}


