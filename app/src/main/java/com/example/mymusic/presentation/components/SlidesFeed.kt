package com.example.mymusic.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mymusic.domain.model.Track
import com.example.mymusic.ui.theme.JetcasterSpacing
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SlidesFeed(
    title: String,
    tracks: List<Track>,
    onTrackClick: (Track) -> Unit,
    modifier: Modifier = Modifier
) {
    if (tracks.isEmpty()) return
    
    Column(modifier = modifier) {
        // Section title với style đẹp hơn
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = JetcasterSpacing.md, vertical = JetcasterSpacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
        
        // Horizontal pager for slides
        val pagerState = rememberPagerState(pageCount = { tracks.size })
        
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .padding(horizontal = JetcasterSpacing.md),
            pageSpacing = 16.dp
        ) { page ->
            val track = tracks[page]
            SlideCard(
                track = track,
                onClick = { onTrackClick(track) },
                modifier = Modifier.fillMaxSize()
            )
        }
        
        // Bỏ page indicator để giao diện sạch hơn
        
        Spacer(modifier = Modifier.height(JetcasterSpacing.md))
    }
}

@Composable
private fun SlideCard(
    track: Track,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background image
            AsyncImage(
                model = track.artworkUrl,
                contentDescription = "Artwork for ${track.title}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Gradient overlay for better text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.8f)
                            )
                        )
                    )
            )
            
            // Source indicator ở góc trên phải
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black.copy(alpha = 0.7f)
                    )
                ) {
                    Text(
                        text = if (track.id.startsWith("jam")) "Jamendo" else "Deezer",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            
            // Content overlay với nút Play
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                // Track info
                Text(
                    text = track.title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp
                    ),
                    color = Color.White,
                    maxLines = 2
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = track.artist,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        letterSpacing = (-0.2).sp
                    ),
                    color = Color.White.copy(alpha = 0.9f),
                    maxLines = 1
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Play button
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = "Play Now",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// Compact slides feed for smaller sections
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CompactSlidesFeed(
    title: String,
    tracks: List<Track>,
    onTrackClick: (Track) -> Unit,
    modifier: Modifier = Modifier
) {
    if (tracks.isEmpty()) return
    
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = JetcasterSpacing.md, vertical = JetcasterSpacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.3).sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
        
        val pagerState = rememberPagerState(pageCount = { tracks.size })
        
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = JetcasterSpacing.md),
            pageSpacing = 12.dp
        ) { page ->
            val track = tracks[page]
            CompactSlideCard(
                track = track,
                onClick = { onTrackClick(track) },
                modifier = Modifier.fillMaxSize()
            )
        }
        
        Spacer(modifier = Modifier.height(JetcasterSpacing.sm))
    }
}

// Latest Tracks slides feed - sử dụng các ô vuông (square cards) như Figma
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LatestTracksSlidesFeed(
    title: String,
    tracks: List<Track>,
    onTrackClick: (Track) -> Unit,
    modifier: Modifier = Modifier
) {
    if (tracks.isEmpty()) return
    
    Column(modifier = modifier) {
        // Section title với style đẹp hơn
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = JetcasterSpacing.md, vertical = JetcasterSpacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.3).sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
        
        // Sử dụng LazyRow thay vì HorizontalPager để hiển thị các ô vuông
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = JetcasterSpacing.md),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
        ) {
            items(tracks) { track ->
                LatestTrackSquareCard(
                    track = track,
                    onClick = { onTrackClick(track) },
                    modifier = Modifier.size(width = 140.dp, height = 140.dp) // Square card 140x140
                )
            }
        }
        
        Spacer(modifier = Modifier.height(JetcasterSpacing.sm))
    }
}

@Composable
private fun CompactSlideCard(
    track: Track,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = track.artworkUrl,
                contentDescription = "Artwork for ${track.title}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Source indicator ở góc trên phải
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black.copy(alpha = 0.7f)
                    )
                ) {
                    Text(
                        text = if (track.id.startsWith("jam")) "Jamendo" else "Deezer",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            
            // Gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.6f)
                            )
                        )
                    )
            )
            
            // Content với nút Play
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = track.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = track.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f),
                    maxLines = 1
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Play button nhỏ hơn
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.size(width = 80.dp, height = 32.dp)
                ) {
                    Text(
                        text = "Play",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun LatestTrackSquareCard(
    track: Track,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp), // Bo góc nhỏ hơn
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // Shadow nhẹ hơn
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = track.artworkUrl,
                contentDescription = "Artwork for ${track.title}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Source indicator ở góc trên phải
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black.copy(alpha = 0.7f)
                    )
                ) {
                    Text(
                        text = if (track.id.startsWith("jam")) "Jamendo" else "Deezer",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
            
            // Gradient overlay nhẹ hơn
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.4f) // Alpha thấp hơn
                            )
                        )
                    )
            )
            
            // Play icon ở giữa (theo Figma design)
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Sử dụng icon đơn giản thay vì PlayArrow để tránh lỗi build
                Text(
                    text = "▶",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // Content ở dưới
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp), // Padding nhỏ hơn cho square card
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = track.title,
                    style = MaterialTheme.typography.bodySmall, // Font size nhỏ hơn cho square card
                    color = Color.White,
                    fontWeight = FontWeight.Medium, // Font weight nhẹ hơn
                    maxLines = 1 // Chỉ 1 dòng cho square card
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Text(
                    text = track.artist,
                    style = MaterialTheme.typography.labelSmall, // Font size nhỏ hơn
                    color = Color.White.copy(alpha = 0.7f), // Alpha thấp hơn
                    maxLines = 1
                )
            }
        }
    }
}
