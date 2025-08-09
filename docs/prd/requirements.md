# Requirements

## Functional (FR)
- FR1: Load and display Top Trending Tracks with images, titles, artists, duration.
- FR2: Load and display Top Trending Albums with cover art and key metadata.
- FR3: Load and display Top Trending Playlists with artwork and track counts.
- FR4: Load and display Top Trending Podcasts (subject to API availability).
- FR5: Load and display Top Feeds (featured/curated as supported by API).
- FR6: Display Radio categories and list playable radio stations.
- FR7: Display Stream categories and list playable streams (if distinct from radio).
- FR8: Provide Explore list with infinite scroll using Paging 3.
- FR9: On tapping a track (or playable item), open Player screen and start playback.
- FR10: Provide Player controls: play/pause, seek, next/previous (where applicable).
- FR11: Support background playback via Media3 foreground service + notification.
- FR12: Show a mini-player in the app when media is playing.
- FR13: Support audio focus handling and resilience to interruptions.
- FR14: Allow downloading a track to device storage with progress and completion state.
- FR15: Handle required runtime permissions and foreground service notifications.
- FR16: Show loading, error, and empty states for all lists.

## Non Functional (NFR)
- NFR1: 100% Kotlin codebase.
- NFR2: UI built entirely with Jetpack Compose (Material 3, Navigation, Paging Compose).
- NFR3: Dependency Injection via Hilt with modular DI graph.
- NFR4: MVVM + Clean Architecture separation (presentation/domain/data).
- NFR5: Use Retrofit + OkHttp + Kotlinx Serialization for network and JSON.
- NFR6: Use Media3 (ExoPlayer) for audio playback.
- NFR7: Use Coil for image loading and caching.
- NFR8: Provide smooth scrolling performance with paging and image loading.
- NFR9: Adhere to scoped storage; use DownloadManager/MediaStore where applicable.
- NFR10: Provide basic offline resilience (transient cache where reasonable).
- NFR11: minSdk 24 (proposed), targetSdk latest stable.
- NFR12: Implement basic analytics/logging and error handling.
- NFR13: Accessibility considerations aligned with WCAG AA where feasible.
