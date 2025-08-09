# 8) Playback Architecture (Media3)
- Player: Media3 ExoPlayer singleton scoped to `@Singleton` via Hilt (or service lifecycle)
- Session: `MediaSession` managed in a `MediaSessionService` (or `MediaLibraryService` if browsable)
- Notification: Media3 `PlayerNotificationManager` replacement (Media3 Notification APIs)
- Audio focus: handled by Media3; configure attributes (usage=media, contentType=music)
- DataSource: Default factories (HTTP stream via OkHttpDataSource optional)
- Queue: Build playlist for albums/playlists; for radio/streams single item live
- UI integration: PlayerViewModel exposes state (isPlaying, position, duration)
- Background: Foreground service for playback with persistent notification

Key classes:
- `PlaybackService` (extends `MediaSessionService`)
- `PlaybackController` (facade used by ViewModels)
- `NowPlayingState` (immutable UI state)
