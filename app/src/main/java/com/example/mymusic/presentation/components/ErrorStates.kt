package com.example.mymusic.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mymusic.ui.theme.JetcasterSpacing

@Composable
fun EnhancedErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    isNetworkError: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (isNetworkError) Icons.Default.WifiOff else Icons.Default.Error,
            contentDescription = "Error",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )
        
        Spacer(modifier = Modifier.height(JetcasterSpacing.md))
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = JetcasterSpacing.lg)
        )
        
        Spacer(modifier = Modifier.height(JetcasterSpacing.lg))
        
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Retry",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(JetcasterSpacing.sm))
            Text("Retry")
        }
    }
}

@Composable
fun NetworkErrorState(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    EnhancedErrorState(
        message = "No internet connection. Please check your network and try again.",
        onRetry = onRetry,
        modifier = modifier,
        isNetworkError = true
    )
}

@Composable
fun ServerErrorState(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    EnhancedErrorState(
        message = "Something went wrong on our end. Please try again later.",
        onRetry = onRetry,
        modifier = modifier
    )
}

@Composable
fun EmptyState(
    message: String,
    modifier: Modifier = Modifier,
    action: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = "Empty",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(JetcasterSpacing.md))
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = JetcasterSpacing.lg)
        )
        
        if (action != null) {
            Spacer(modifier = Modifier.height(JetcasterSpacing.lg))
            action()
        }
    }
}

@Composable
fun SearchEmptyState(
    query: String,
    onClearSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = "No results",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(JetcasterSpacing.md))
        
        Text(
            text = "No results found for '$query'",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = JetcasterSpacing.lg)
        )
        
        Spacer(modifier = Modifier.height(JetcasterSpacing.md))
        
        Text(
            text = "Try adjusting your search terms or filters",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = JetcasterSpacing.lg)
        )
        
        Spacer(modifier = Modifier.height(JetcasterSpacing.lg))
        
        OutlinedButton(onClick = onClearSearch) {
            Text("Clear Search")
        }
    }
}

@Composable
fun OfflineState(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    EnhancedErrorState(
        message = "You're offline. Some features may not be available.",
        onRetry = onRetry,
        modifier = modifier,
        isNetworkError = true
    )
}
