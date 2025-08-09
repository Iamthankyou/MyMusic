# Music App — Brainstorming Documentation

## Overview
- **Goal**: Build a Music app using 100% Kotlin, Jetpack Compose UI, Hilt DI, MVVM + Clean Architecture, powered by Jamendo API.
- **API**: `https://api.jamendo.com/v3.0` with `client_id=05c6ee3`
- **Core Features**:
  - Top trending: Track, Album, Playlist, Podcast
  - Top Feeds
  - Category: Radio
  - Category: Stream
  - Explore list with lazy paging (infinite scroll)
  - Player screen on item click (background playback, notification controls)
  - Download track to device storage
  - Modern, beautiful UI

## Non-Functional Requirements
- Kotlin-only codebase
- Jetpack Compose for all UI
- Hilt for dependency injection
- MVVM + Clean Architecture
- Offline-friendly where possible (caching where reasonable)
- Minimum SDK: TBD (suggest 24+); Target SDK: latest

## High-Level Architecture
```
app (Android/UI layer)
  ├─ presentation (Compose screens, ViewModels)
  ├─ navigation
  └─ di (Hilt modules)

domain (pure Kotlin)
  ├─ model (entities)
  ├─ repository (interfaces)
  └─ usecase (interactors)

data (Android/Kotlin)
  ├─ remote (Retrofit services, DTOs)
  ├─ repository (impl)
  ├─ mapper
  └─ local (cache, optional Room/DataStore)

media (optional separate module)
  ├─ playback (Media3/ExoPlayer, service, notification)
  └─ download (DownloadManager/MediaStore)
```

## Tech Stack
- Kotlin, Coroutines, Flow
- Jetpack Compose (Material 3, Navigation, Paging 3 Compose)
- Hilt DI
- Retrofit + OkHttp (logging) + Kotlinx Serialization or Moshi (choose one)
- Paging 3
- Media3 (ExoPlayer) for playback
- Coil for image loading
- Accompanist Permissions (or Compose runtime permissions)
- AndroidX WorkManager (optional for downloads/cleanup)

## API Integration (Jamendo)
- Base: `https://api.jamendo.com/v3.0`
- Auth: `client_id` query parameter
- Common params: `format=json`, pagination params
- Endpoints (to be verified in docs):
  - Trending Tracks: `/tracks` with ordering params (e.g., popularity)
  - Trending Albums: `/albums`
  - Trending Playlists: `/playlists`
  - Podcasts: `/podcasts` (verify availability)
  - Radios: `/radios`
  - Streams: (verify—may map to radios/stream endpoints)
  - Feeds/Featured: (verify—for curated/top feeds)
- Notes: Confirm exact query parameters for sorting, paging (e.g., `limit`, `offset`), audio preview URLs, and cover art URLs.

## Data Flow
1. Presentation requests data via ViewModel
2. ViewModel uses UseCases (domain)
3. UseCases call Repository interfaces
4. Data layer repositories fetch from Remote (Retrofit) and optional Local cache
5. Mappers convert DTO ↔ Domain models
6. ViewModel exposes State (StateFlow) for Compose UI

## Paging Strategy
- Use Paging 3 with `PagingSource` for:
  - Explore list
  - Trending lists (tracks, albums, playlists, podcasts)
- Trigger load on end-of-list scroll
- Use `cachedIn(viewModelScope)`

## Playback
- Media3 + ExoPlayer
- Foreground service with MediaSession for background playback
- Notification controls, lockscreen controls
- Integrate with Compose Player screen (play/pause/seek/next/prev, artwork)
- Handle audio focus and lifecycle

## Downloading
- Use Android `DownloadManager` or direct download + `MediaStore` insert
- Scoped Storage compliant (Android 10+)
- Runtime permissions for notifications/storage (as needed per SDK)
- Show progress and completion
- Store metadata for downloaded tracks

## Navigation
- Compose Navigation graph:
  - Home (tabs or sections): Trending, Feeds, Radio, Stream, Explore
  - Details: Track/Album/Playlist/Podcast details
  - Player Screen (full-screen player)
  - Downloads

## UI/UX Notes
- Material 3 theming, dynamic color support
- List cells: cover image, title, subtitle, action (play, more)
- Shimmer/loading placeholder while fetching
- Error and empty states
- Pull-to-refresh where applicable

## Domain Models (examples)
- `Track(id, title, artist, albumId, duration, artworkUrl, audioUrl, isDownloadable)`
- `Album(id, title, artist, trackCount, artworkUrl)`
- `Playlist(id, title, trackCount, artworkUrl)`
- `Podcast(id, title, episodes, artworkUrl)` (if supported)
- `Radio(id, name, streamUrl, artworkUrl)`

## Repository Interfaces (domain)
- `TrackRepository`
  - `getTrendingTracks(paging: PagingParams): Flow<PagingData<Track>>`
  - `getTrackDetail(id: String): Flow<Track>`
- `AlbumRepository`
- `PlaylistRepository`
- `PodcastRepository` (if supported)
- `RadioRepository`
- `ExploreRepository`
- `DownloadRepository`

## Use Cases (domain)
- `GetTrendingTracksUseCase`
- `GetTrendingAlbumsUseCase`
- `GetTrendingPlaylistsUseCase`
- `GetTrendingPodcastsUseCase`
- `GetTopFeedsUseCase`
- `GetRadioCategoriesUseCase`
- `GetStreamCategoriesUseCase`
- `GetExplorePagingUseCase`
- `GetTrackDetailUseCase`
- `StartPlaybackUseCase`
- `DownloadTrackUseCase`

## Remote Layer (data)
- Retrofit service interfaces per resource
- DTOs matching Jamendo responses
- Mappers to domain models
- PagingSources for lists

## DI (Hilt)
- AppModule: Retrofit, OkHttp, Serialization, ImageLoader
- RepositoryModule: bind repo interfaces to impls
- UseCaseModule: provide use cases
- MediaModule: ExoPlayer, MediaSession

## Permissions
- Post Notifications (Android 13+)
- Foreground service (media)
- Manage downloads
- Network state (optional)

## Open Questions / To Verify
- Exact Jamendo endpoints and parameters for: feeds, streams, podcasts
- Caching strategy (Room vs in-memory)
- Min/Target SDK versions
- Offline playback requirements

## Milestones (Draft)
1) Project setup: Gradle, Hilt, Compose, Navigation, Retrofit, Media3, Paging
2) API client + basic models for Tracks/Albums/Playlists
3) Trending screens (lists + paging)
4) Player service + UI + notification controls
5) Explore paging
6) Radio/Stream categories
7) Downloads
8) Polish: error states, theming, animations

## Risks
- API variability/limits (rate limiting, auth changes)
- Background playback edge cases
- Scoped storage differences across Android versions

## Next Steps
- Confirm open questions
- Lock SDK versions
- Implement project scaffolding and DI
- Build sample screen (Trending Tracks) end-to-end
