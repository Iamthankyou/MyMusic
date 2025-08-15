# Download UI Components Specification

## Overview
This document specifies the UI components, states, and interactions for the download feature in MyMusic app.

## Core Download Components

### 1. Download Button
**Purpose**: Primary interface for initiating downloads

**States**:
- **Default**: Download icon + "Download" text
- **Downloading**: Spinner + "Downloading..." text
- **Downloaded**: Checkmark + "Downloaded" text
- **Failed**: Exclamation + "Retry" text

**Behavior**:
- Tap to start download
- Long press for download options (quality, location)
- Shows progress indicator during download
- Changes to play button when downloaded

**Code Structure**:
```kotlin
@Composable
fun DownloadButton(
    track: Track,
    downloadState: DownloadState,
    onDownloadClick: () -> Unit,
    onPlayClick: () -> Unit
) {
    when (downloadState) {
        is DownloadState.NotDownloaded -> DownloadIconButton(onClick = onDownloadClick)
        is DownloadState.Downloading -> DownloadingButton(progress = downloadState.progress)
        is DownloadState.Downloaded -> PlayButton(onClick = onPlayClick)
        is DownloadState.Failed -> RetryButton(onClick = onDownloadClick)
    }
}
```

### 2. Download Progress Indicator
**Purpose**: Show download progress to users

**Visual Elements**:
- **Progress Bar**: Linear progress bar with percentage
- **Status Text**: Current status (e.g., "Downloading 45%")
- **Speed Info**: Download speed and ETA
- **Cancel Button**: Stop download option

**States**:
- **Indeterminate**: Shimmer effect for unknown progress
- **Determinate**: Actual progress percentage
- **Paused**: Show pause indicator
- **Completed**: Success animation

**Code Structure**:
```kotlin
@Composable
fun DownloadProgressIndicator(
    progress: Float,
    status: String,
    speed: String?,
    onCancel: () -> Unit
) {
    Column {
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = status)
            speed?.let { Text(text = it) }
            IconButton(onClick = onCancel) {
                Icon(Icons.Default.Close, "Cancel")
            }
        }
    }
}
```

### 3. Download Management Screen
**Purpose**: Central location for managing all downloads

**Layout Structure**:
```
┌─────────────────────────────────┐
│ Downloads                    ⚙️ │ ← Header with settings
├─────────────────────────────────┤
│ [Search downloads...]           │ ← Search bar
├─────────────────────────────────┤
│ Active Downloads                │ ← Section header
│ ┌─────────────────────────────┐ │
│ │ 🎵 Track Title - Artist    │ │ ← Download item
│ │ ████████████░░░░ 75%       │ │
│ │ 2.3 MB / 3.1 MB • 15s left│ │
│ └─────────────────────────────┘ │
├─────────────────────────────────┤
│ Downloaded Tracks               │ ← Section header
│ ┌─────────────────────────────┐ │
│ │ 🎵 Track Title - Artist    │ │ ← Downloaded item
│ │ ✅ Downloaded • 3.1 MB     │ │
│ │ [Play] [Delete] [Share]    │ │
│ └─────────────────────────────┘ │
└─────────────────────────────────┘
```

**Download Item States**:
- **Pending**: Clock icon + "Queued" status
- **Downloading**: Progress bar + percentage + speed
- **Paused**: Pause icon + "Paused" status
- **Completed**: Checkmark + file size + date
- **Failed**: Error icon + "Failed" + retry button

### 4. Download Queue Management
**Purpose**: Control download order and priority

**Features**:
- **Drag & Drop**: Reorder download priority
- **Batch Operations**: Select multiple downloads
- **Priority Levels**: High, Normal, Low priority
- **Queue Limits**: Maximum concurrent downloads

**Code Structure**:
```kotlin
@Composable
fun DownloadQueue(
    downloads: List<DownloadItem>,
    onReorder: (from: Int, to: Int) -> Unit,
    onPriorityChange: (id: String, priority: Priority) -> Unit
) {
    LazyColumn {
        items(
            items = downloads,
            key = { it.id }
        ) { download ->
            DownloadQueueItem(
                download = download,
                onPriorityChange = { priority ->
                    onPriorityChange(download.id, priority)
                }
            )
        }
    }
}
```

## Integration Points

### 1. Track List Items
**Download Button Placement**:
- **Primary Action**: Right side of track item
- **Secondary Action**: In track options menu
- **Visual Hierarchy**: Consistent with other action buttons

**States Integration**:
- Show download status in track item
- Indicate offline availability
- Display download progress inline

### 2. Player Screen
**Download Integration**:
- **Download Button**: Prominent placement near play controls
- **Progress Display**: Show download progress in player
- **Offline Indicator**: Highlight when track is available offline

**Code Structure**:
```kotlin
@Composable
fun PlayerDownloadSection(
    track: Track,
    downloadState: DownloadState,
    onDownload: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Other player controls...
        
        DownloadButton(
            track = track,
            downloadState = downloadState,
            onDownloadClick = onDownload
        )
        
        // More controls...
    }
}
```

### 3. Search Results
**Download Indicators**:
- **Download Status**: Show which tracks are downloaded
- **Download Button**: Allow downloading from search results
- **Offline Filter**: Filter to show only downloaded tracks

## User Experience Patterns

### 1. Download Initiation
**Flow**:
1. User taps download button
2. Show download confirmation dialog
3. Display download options (quality, location)
4. Start download with progress feedback
5. Show success/error notification

**Confirmation Dialog**:
```kotlin
@Composable
fun DownloadConfirmationDialog(
    track: Track,
    onConfirm: (DownloadOptions) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Download ${track.title}?") },
        text = { 
            DownloadOptionsSelector(
                onOptionsSelected = onConfirm
            )
        },
        confirmButton = { Text("Download") },
        dismissButton = { Text("Cancel") }
    )
}
```

### 2. Progress Feedback
**Real-time Updates**:
- **Progress Bar**: Update every 100ms
- **Speed Display**: Update every 1 second
- **ETA Calculation**: Update every 5 seconds
- **Status Changes**: Immediate notification

### 3. Error Handling
**User Communication**:
- **Clear Messages**: Explain what went wrong
- **Actionable Solutions**: Provide retry options
- **Context Information**: Show relevant details
- **Recovery Path**: Guide user to resolution

## Accessibility Features

### 1. Screen Reader Support
- **Content Descriptions**: Clear download status descriptions
- **Progress Announcements**: Announce progress changes
- **Action Labels**: Descriptive button labels

### 2. Visual Accessibility
- **High Contrast**: Ensure visibility in all states
- **Color Independence**: Don't rely solely on color
- **Size Variations**: Support different text sizes

### 3. Interaction Accessibility
- **Touch Targets**: Minimum 48dp touch targets
- **Keyboard Navigation**: Full keyboard support
- **Voice Control**: Voice command compatibility

## Animation & Transitions

### 1. State Transitions
- **Smooth Transitions**: Animate between download states
- **Progress Animations**: Smooth progress bar updates
- **Success Animations**: Celebration for completed downloads

### 2. Loading States
- **Shimmer Effects**: For indeterminate progress
- **Pulse Animations**: For active downloads
- **Fade Transitions**: Between different states

## Performance Considerations

### 1. UI Updates
- **Throttled Updates**: Limit progress updates to prevent lag
- **Background Processing**: Handle downloads off main thread
- **Memory Management**: Efficient list rendering for large downloads

### 2. State Management
- **Efficient State**: Minimize state updates
- **Lazy Loading**: Load download data on demand
- **Caching**: Cache frequently accessed download info

## Testing Scenarios

### 1. UI State Testing
- **Download Button States**: Verify all states display correctly
- **Progress Updates**: Test progress indicator accuracy
- **Error States**: Verify error handling UI

### 2. User Interaction Testing
- **Download Flow**: Test complete download process
- **Queue Management**: Test download queue operations
- **Settings Integration**: Test download preferences

### 3. Accessibility Testing
- **Screen Reader**: Verify screen reader compatibility
- **Keyboard Navigation**: Test keyboard-only operation
- **Visual Contrast**: Ensure sufficient contrast ratios
