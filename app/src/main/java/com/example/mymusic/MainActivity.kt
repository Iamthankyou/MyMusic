package com.example.mymusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mymusic.presentation.home.trending.TrendingScreen
import com.example.mymusic.presentation.player.MiniPlayer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color = MaterialTheme.colorScheme.background) {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { MiniPlayer() }
                ) { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = "trending",
                        modifier = Modifier.padding(padding)
                    ) {
                        composable("trending") { TrendingScreen() }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TrendingPreview() {}