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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mymusic.presentation.player.MiniPlayer
import com.example.mymusic.presentation.navigation.BottomNavBar
import com.example.mymusic.presentation.navigation.NavGraph
import com.example.mymusic.ui.theme.JetcasterTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetcasterTheme {
                val navController = rememberNavController()
                val currentRoute by navController.currentBackStackEntryAsState()
                val isPlayerScreen = currentRoute?.destination?.route == "player"
                
                Scaffold(
                    bottomBar = {
                        Column {
                            if (!isPlayerScreen) {
                                MiniPlayer(navController = navController)
                            }
                            BottomNavBar(navController)
                        }
                    }
                ) { padding ->
                    NavGraph(
                        navController = navController,
                        modifier = Modifier.padding(padding)
                    )
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