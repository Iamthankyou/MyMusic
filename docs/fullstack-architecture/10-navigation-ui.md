# 10) Navigation & UI
Navigation Destinations (Compose):
- `TrendingRoute` (tabs: Tracks/Albums/Playlists/Podcasts)
- `FeedsRoute`
- `RadioRoute`
- `StreamRoute`
- `ExploreRoute`
- `DetailsRoute(type, id)`
- `PlayerRoute`
- `DownloadsRoute`

Scaffold:
- `Scaffold(bottomBar = BottomNav, topBar per screen, content)`
- `MiniPlayer` anchored above BottomNav

State management:
- ViewModels per screen, injected via Hilt
- `StateFlow`/`UiState` immutables; `collectAsStateWithLifecycle()` in Compose
