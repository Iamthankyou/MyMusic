# Project Brief — Music App (Jamendo)

## Summary
A Kotlin-based Android Music app using Jetpack Compose, Hilt, MVVM + Clean Architecture. Integrates Jamendo API (`client_id=05c6ee3`) to provide trending content (tracks, albums, playlists, podcasts), feeds, radio/stream categories, explore with infinite scrolling, playback with Media3, and downloads to device storage.

## Goals
- Deliver a modern, performant Music app with beautiful UI.
- Provide discovery (trending, feeds, explore) and playback capabilities.
- Support background playback and downloads compliant with scoped storage.

## Non-Functional Requirements
- 100% Kotlin.
- Jetpack Compose UI only.
- DI with Hilt.
- MVVM + Clean Architecture.
- Offline-friendly where feasible (basic caching).
- minSdk: 24 (proposed), targetSdk: latest.

## Primary Users & Use Cases
- Casual listeners discovering new music via trending/feeds/explore.
- Users who want background playback and ability to download tracks for offline listening.

## Key Features (In Scope)
- Trending: Tracks, Albums, Playlists, Podcasts (as supported by Jamendo).
- Top Feeds (curated/featured as available).
- Radio and Stream categories.
- Explore list with Paging 3 (infinite scroll).
- Player screen with Media3 (play/pause/seek/next/prev, notification, lockscreen).
- Download tracks to storage with progress and completion feedback.

## Out of Scope (Initial)
- Advanced library management (custom playlists, favorites sync).
- Social/sharing features.
- Full offline cache of all categories.

## Technical Stack
- Kotlin, Coroutines/Flow
- Jetpack Compose (Material3, Navigation, Paging 3 Compose)
- Hilt DI
- Retrofit + OkHttp + Kotlinx Serialization
- Media3 (ExoPlayer)
- Coil (images)
- Accompanist Permissions (as needed)

## Integrations
- Jamendo API: `https://api.jamendo.com/v3.0` (query param `client_id`).
- Verify endpoints/params for feeds/streams/podcasts; confirm paging (`limit`, `offset`).

## Risks & Constraints
- API rate limits/availability of specific endpoints.
- Scoped storage + background service behavior across Android versions.
- Playback edge cases (audio focus, interruptions).

## Milestones (High-Level)
1) Project setup: Gradle, dependencies, DI, Navigation, theming.
2) API client + domain models + repositories.
3) Trending (Tracks) end-to-end + Player (MVP).
4) Expand to Albums/Playlists/Podcasts + Explore paging.
5) Radio/Stream categories.
6) Downloads + UX polish.

## Success Criteria
- Smooth infinite scrolling lists with images.
- Stable playback with background controls.
- Successful download flow on supported devices.
- Clean architecture separation (presentation/domain/data) and testable components.
