# Goals and Background Context

## Goals
- Deliver a modern, performant Android Music app with beautiful UI.
- Provide discovery via Trending, Feeds, Explore with infinite scroll.
- Enable stable background playback with Media3 and notification controls.
- Allow users to download tracks to device storage with progress.

## Background Context
MyMusic is a Kotlin-only Android app using Jetpack Compose for all UI and Hilt for dependency injection, structured with MVVM and Clean Architecture. Content is powered by the Jamendo API (`https://api.jamendo.com/v3.0`, `client_id=05c6ee3`) to surface trending tracks, albums, playlists, podcasts (as available), curated feeds, radio and stream categories, as well as an Explore experience with infinite scrolling. The product focuses on a smooth listening and discovery experience, supporting background playback and compliant downloads on modern Android versions.

The MVP emphasizes an end-to-end slice: project setup, API integration, a Trending Tracks list with paging and images, a functional media player with background controls, and a basic download flow. Subsequent iterations expand to more content types (albums/playlists/podcasts), radio/stream categories, and polish.

## Change Log
| Date       | Version | Description                                 | Author |
|------------|---------|---------------------------------------------|--------|
| 2025-08-09 | 0.1     | Initial draft generated from project brief  | Duy LQ     |
