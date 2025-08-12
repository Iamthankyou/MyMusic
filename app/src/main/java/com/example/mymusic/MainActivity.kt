package com.example.mymusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mymusic.presentation.home.trending.TrendingScreen
import com.example.mymusic.presentation.player.MiniPlayer
import com.example.mymusic.presentation.navigation.BottomNavBar
import com.example.mymusic.ui.theme.MyMusicTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyMusicTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        Column {
                            MiniPlayer()
                            BottomNavBar(navController)
                        }
                    }
                ) { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = "trending",
                        modifier = Modifier.padding(padding)
                    ) {
                        composable("trending") { TrendingScreen() }
                        composable("explore") { PlaceholderScreen(title = "Explore") }
                        composable("downloads") { PlaceholderScreen(title = "Downloads") }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TrendingPreview() {}

@Composable
private fun PlaceholderScreen(title: String) {
    Surface(color = MaterialTheme.colorScheme.background) {
        androidx.compose.material3.Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}