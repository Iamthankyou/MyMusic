package com.example.mymusic.presentation.detail.components

import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun ShareButton(
    title: String,
    artist: String,
    url: String
) {
    val context = LocalContext.current
    
    IconButton(
        onClick = {
            val shareText = "Check out '$title' by $artist: $url"
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, "Share Track")
            context.startActivity(shareIntent)
        }
    ) {
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Share"
        )
    }
}
