# MyMusic — Full-Stack Architecture (Android Client)

References:
- PRD: `docs/prd.md`
- Front-End Spec: `docs/front-end-spec.md`
- API: `https://api.jamendo.com/v3.0` with `client_id=05c6ee3`

## 1) Executive Summary
MyMusic is an Android-only Kotlin app built with Jetpack Compose and Hilt, following MVVM + Clean Architecture. It integrates Jamendo API to provide discovery (Trending, Feeds, Radio/Stream, Explore) and listening features (Media3 playback, downloads). The architecture prioritizes modularity, testability, smooth UI performance, and background-safe media playback.

## 2) High-Level Architecture
Layers:
- Presentation (Compose UI + ViewModels)
- Domain (Use Cases + Entities + Repository interfaces)
- Data (Repository implementations + Remote + Local)
- Media (Playback service, session, notifications)
- Download (DownloadManager + MediaStore integration)

Recommended initial module: single `app` module with clear package boundaries. Optional future split:
- `:app` (presentation + navigation)
- `:domain` (use cases + entities + repo contracts)
- `:data` (retrofit services, mappers, repo impls)
- `:media` (player service + playback)

## 3) Package Structure (within `app` for MVP)
- `com.example.mymusic.presentation`
  - `home` (Trending/Feeds/Radio/Stream/Explore screens)
  - `details` (Album/Playlist/Podcast details)
  - `player` (full-screen player + mini-player UI)
  - `downloads` (download list/progress)
  - `components` (shared Compose components)
  - `navigation` (NavGraph)
  - `theme`
- `com.example.mymusic.domain`
  - `model`
  - `repository`
  - `usecase`
- `com.example.mymusic.data`
  - `remote` (Retrofit services, DTOs)
  - `repository` (impls)
  - `mapper`
  - `local` (optional cache/Room/DataStore)
- `com.example.mymusic.media` (Media3 service, session, player facade)
- `com.example.mymusic.download` (download controller, receivers)
- `com.example.mymusic.di` (Hilt modules)

## 4) Dependencies (Gradle concept list)
- Kotlin, Coroutines, Flow
- Jetpack Compose (Material3, Navigation, Paging Compose)
- Hilt (Dagger/Hilt)
- Retrofit + OkHttp + Kotlinx Serialization
- Paging 3
- Media3 (ExoPlayer, MediaSession)
- Coil (images)
- Accompanist Permissions (as needed)
- WorkManager (optional for downloads/cleanup)

## 5) Networking & API
- Base URL: `https://api.jamendo.com/v3.0/`
- Auth: query param `client_id=05c6ee3`
- Common params: `format=json`, paging via `limit` and `offset`
- HTTP client: OkHttp with logging interceptor (debug), timeout 20–30s
- Serialization: Kotlinx Serialization (Json { ignoreUnknownKeys = true })
- Error handling: map HTTP/network errors to domain `AppError`

Retrofit service interfaces (examples):
- `JamendoTracksService`: `GET /tracks`
- `JamendoAlbumsService`: `GET /albums`
- `JamendoPlaylistsService`: `GET /playlists`
- `JamendoRadiosService`: `GET /radios`
- `JamendoPodcastsService`: `GET /podcasts` (verify availability)
- `JamendoFeedsService`: `GET /feeds` or appropriate endpoint (verify)

DTO → Domain mappers ensure presentation is decoupled from API shapes.

## 6) Domain Layer
Entities (examples):
- `Track(id, title, artist, albumId, durationMs, artworkUrl, audioUrl, isDownloadable)`
- `Album(id, title, artist, trackCount, artworkUrl)`
- `Playlist(id, title, trackCount, artworkUrl)`
- `Podcast(id, title, episodeCount, artworkUrl)` (if supported)
- `Radio(id, name, streamUrl, artworkUrl)`

Repository Contracts:
- `TrackRepository` (trending, detail)
- `AlbumRepository`
- `PlaylistRepository`
- `PodcastRepository` (optional)
- `RadioRepository`
- `ExploreRepository`
- `DownloadRepository`

Use Cases:
- `GetTrendingTracksUseCase`, `GetTrendingAlbumsUseCase`, `GetTrendingPlaylistsUseCase`, `GetTrendingPodcastsUseCase`
- `GetTopFeedsUseCase`
- `GetRadioCategoriesUseCase`, `GetStreamCategoriesUseCase`
- `GetExplorePagingUseCase`
- `GetTrackDetailUseCase`
- `StartPlaybackUseCase`, `TogglePlayPauseUseCase`, `SeekUseCase`, `SkipNextUseCase`, `SkipPrevUseCase`
- `DownloadTrackUseCase`, `ObserveDownloadsUseCase`

## 7) Data Layer
- Remote: Retrofit services + request builders injecting `client_id` and defaults via Interceptor
- Local: Optional caching (Room) for lists or last playback state (MVP: in-memory)
- Repository Implementations: Combine remote/local; expose `Flow`/`PagingData`
- Paging: `PagingSource`/`RemoteMediator` (start with simple `PagingSource` using `offset`/`limit`)

Paging Sources (examples):
- `TrendingTracksPagingSource`
- `TrendingAlbumsPagingSource`
- `ExplorePagingSource`

## 8) Playback Architecture (Media3)
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

## 9) Download Architecture
- Use Android `DownloadManager` for track audio URLs; register `BroadcastReceiver` for completion
- Target storage: Scoped `MediaStore.Audio` (Android 10+) with metadata
- Track progress: Query `DownloadManager` for status; expose via `Flow`
- Cancel/resume: Use `DownloadManager` APIs
- Map downloaded file URIs back to domain model for offline playback

Key classes:
- `DownloadController` (start/cancel/query)
- `DownloadRepository` (domain bridge)
- `DownloadReceiver` (completion)

## 10) Navigation & UI
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

## 11) DI Graph (Hilt)
Modules:
- `NetworkModule`: OkHttp, Retrofit, Json, Services
- `RepositoryModule`: Binds repo interfaces to impls
- `UseCaseModule`: Provides use cases
- `MediaModule`: ExoPlayer, MediaSession, PlaybackController
- `DownloadModule`: DownloadManager, DownloadController
- `ImageModule`: Coil ImageLoader

Scopes:
- `@Singleton` for Retrofit, Repositories, ExoPlayer, Controllers
- `@ViewModelScoped` for use cases if preferred, otherwise singleton providers

## 12) Error Handling & Resilience
- Central `AppError` (Network, Api, NotFound, Unauthorized, Playback, Download, Unknown)
- Map exceptions to `AppError` at repository boundary
- UI shows error states with retry actions; toasts/snackbars for transient failures
- Backoff/retry strategy for paging loads on network errors

## 13) Configuration & Secrets
- `client_id` stored in `local.properties` or `gradle.properties` and injected via BuildConfig (`BuildConfig.JAMENDO_CLIENT_ID`)
- Debug logging enabled only in debug builds (OkHttp logging)

## 14) Permissions & Policies
- Post Notifications (Android 13+)
- Foreground service for media playback
- Network state (optional)
- Storage: Use `MediaStore` (no broad external storage permission for Android 10+)

## 15) Performance
- Paging prefetch distance 2–3; `cachedIn(viewModelScope)`
- Image sizes constrained; Coil crossfade + placeholders
- Avoid heavy recomposition; use item keys; snapshotFlow for scroll-derived state
- Player off main thread; decode/render handled by Media3

## 16) Analytics & Telemetry (optional)
- Log events: list_impression, item_click, playback_start/stop, download_start/complete/error

## 17) Testing Strategy
- Unit tests: mappers, use cases, simple repos with fakes
- Instrumented: ViewModels with coroutine testing; navigation smoke
- UI tests (Compose): key flows (Trending → Play, Download)

## 18) Build & Release
- Build variants: debug/release; enable R8 shrink for release
- Proguard: keep Media3/OkHttp/Retrofit models as needed

## 19) Implementation Plan (Matches Epics)
1. Foundation & Trending Tracks MVP
   - Gradle deps, Hilt setup, Theme, Navigation scaffold
   - Retrofit/OkHttp/Json, Track service + repo + use case
   - Trending Tracks screen with Paging
   - Minimal Player (service + controls)
2. Media Playback & Player UX
   - Foreground service, notification, audio focus
   - Mini-player + full player UX polish
3. Explore & Content Expansion
   - Explore Paging; Albums/Playlists/Podcasts lists & details
4. Radio & Stream
   - Radio/Stream categories, playback integration
5. Downloads
   - Download flow + Downloads screen + completion
6. Polish & Accessibility
   - Error/empty states, animations, accessibility checks

## 20) Open Questions
- Exact Jamendo endpoints/params for Feeds/Podcasts/Streams
- Licensing/attribution requirements for Jamendo content
- Min/Target SDK final values (proposal: min 24, target latest)

## 21) Appendix — Suggested Gradle Snippets (illustrative)
```kotlin
// build.gradle.kts (app) — key libs (versions via BOMs / libs.versions.toml)
dependencies {
  implementation(platform("androidx.compose:compose-bom:<version>"))
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.material3:material3")
  implementation("androidx.navigation:navigation-compose:<version>")
  implementation("androidx.paging:paging-runtime:<version>")
  implementation("androidx.paging:paging-compose:<version>")

  implementation("com.google.dagger:hilt-android:<version>")
  kapt("com.google.dagger:hilt-compiler:<version>")
  implementation("com.squareup.retrofit2:retrofit:<version>")
  implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:<version>")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:<version>")
  implementation("com.squareup.okhttp3:okhttp:<version>")
  implementation("com.squareup.okhttp3:logging-interceptor:<version>")
  implementation("io.coil-kt:coil-compose:<version>")

  implementation("androidx.media3:media3-exoplayer:<version>")
  implementation("androidx.media3:media3-session:<version>")
  implementation("androidx.media3:media3-ui:<version>")
}
```
