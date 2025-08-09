# MyMusic Product Requirements Document (PRD)

## Goals and Background Context

### Goals
- Deliver a modern, performant Android Music app with beautiful UI.
- Provide discovery via Trending, Feeds, Explore with infinite scroll.
- Enable stable background playback with Media3 and notification controls.
- Allow users to download tracks to device storage with progress.

### Background Context
MyMusic is a Kotlin-only Android app using Jetpack Compose for all UI and Hilt for dependency injection, structured with MVVM and Clean Architecture. Content is powered by the Jamendo API (`https://api.jamendo.com/v3.0`, `client_id=0a8b58ca`) to surface trending tracks, albums, playlists, podcasts (as available), curated feeds, radio and stream categories, as well as an Explore experience with infinite scrolling. The product focuses on a smooth listening and discovery experience, supporting background playback and compliant downloads on modern Android versions.

The MVP emphasizes an end-to-end slice: project setup, API integration, a Trending Tracks list with paging and images, a functional media player with background controls, and a basic download flow. Subsequent iterations expand to more content types (albums/playlists/podcasts), radio/stream categories, and polish.

### Change Log
| Date       | Version | Description                                 | Author |
|------------|---------|---------------------------------------------|--------|
| 2025-08-09 | 0.1     | Initial draft generated from project brief  | Duy LQ     |

## Requirements

### Functional (FR)
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

### Non Functional (NFR)
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

## User Interface Design Goals

### Overall UX Vision
A clean, modern, media-first experience using Material 3 with dynamic color, smooth transitions, and responsive list performance. Emphasis on discoverability and quick access to playback.

### Key Interaction Paradigms
- Bottom navigation or top tabs for sections (Trending, Feeds, Radio, Stream, Explore).
- Infinite scroll lists with pull-to-refresh where applicable.
- Mini-player persistent while browsing; tap to expand full Player.

### Core Screens and Views
- Home/Trending (Tracks, Albums, Playlists, Podcasts)
- Feeds
- Radio
- Stream
- Explore
- Player (full-screen)
- Downloads
- Detail screens (Track/Album/Playlist/Podcast)

### Accessibility
WCAG AA (as feasible within Android constraints).

### Branding
Material 3 baseline with custom theming; artwork-forward list items and large headers.

### Target Device and Platforms
Mobile Only (Android).

## Technical Assumptions

### Repository Structure
Monorepo (single Android project). Start with one `app` module; consider splitting `data`/`domain` later.

### Service Architecture
Monolithic Android app with Clean Architecture layers enforced via packages/modules.

### Testing Requirements
Unit + Integration where practical (e.g., ViewModel/unit, repository/integration with fake servers). UI testing added progressively.

### Additional Technical Assumptions and Requests
- Retrofit + OkHttp logging interceptor; Kotlinx Serialization.
- Paging 3 with `PagingSource` and `cachedIn(viewModelScope)`.
- Media3 ExoPlayer, MediaSession, foreground service notification.
- Coil for images; Accompanist Permissions if needed.
- DownloadManager + MediaStore for downloads; WorkManager optional for long-running tasks.
- Compose Navigation for in-app routing; material icons.

## Epic List
- Epic 1: Foundation & Trending Tracks MVP — Project setup, dependencies, DI, Jamendo client, Trending Tracks list with Paging, basic Player.
- Epic 2: Media Playback & Player UX — Foreground service, notification controls, mini-player, robust audio focus and session management.
- Epic 3: Explore & Content Expansion — Explore infinite scroll; add Albums/Playlists/Podcasts lists and details.
- Epic 4: Radio & Stream — Radio and stream categories with playback.
- Epic 5: Downloads — Download flow, progress UI, completed downloads list.
- Epic 6: Polish & Accessibility — Error/empty states, theming, animations, accessibility enhancements.

> Confirm epic list; upon approval, we will expand epic details (stories and acceptance criteria) sequentially.

## Checklist Results Report

- Scope & Goals: PASS — Goals and MVP slice are clear and aligned across docs.
- Functional Requirements: PASS — FR1–FR16 cover required behaviors; streaming/radio distinction noted.
- Non-Functional Requirements: PASS — Tech stack and constraints specified; min/target SDK proposed.
- UI/UX Alignment: PASS — Front-End Spec matches PRD (nav, screens, states, mini-player).
- Architecture Consistency: PASS — DI, modules, Media3, Paging, Download flow consistent with PRD/UX.
- Gaps/Unknowns: NEEDS ACTION — Jamendo endpoints for Feeds/Podcasts/Streams to be verified; licensing/attribution.
- Security/Secrets: PASS — Plan to store `client_id` via BuildConfig/local properties documented.
- Accessibility: PASS — Target WCAG AA guidance present.
- Testability: PASS — Strategy outlined (unit/integration/UI tests) matching architecture.

Action Items:
- A1: Verify Jamendo endpoints and parameters for Feeds, Podcasts, Streams; update PRD/Architecture accordingly.
- A2: Confirm minSdk (24) and targetSdk (latest) in Gradle configs.
- A3: Decide initial module split (stay single `app` vs. early `data/domain` extraction).
- A4: Add attribution/licensing notes for Jamendo content in-app (About or Settings) if required.

Reviewer: PO (Sarah) — Date: 2025-08-09

## Next Steps

### UX Expert Prompt
"Create a concise UI/UX specification for the MyMusic Android app using the PRD. Focus on high-level flows and core screens: Trending, Feeds, Radio, Stream, Explore, Player, Downloads, and Details. Outline navigation model (bottom nav vs tabs), mini-player behavior, loading/error states, and visual style guidelines (Material 3, dynamic color)."

### Architect Prompt
"Create a full-stack (client) architecture for the MyMusic Android app per this PRD: Kotlin-only, Jetpack Compose UI, Hilt DI, MVVM + Clean Architecture, Jamendo API integration, Paging 3, Media3 playback service, and DownloadManager/MediaStore downloads. Provide module structure, DI graph, network stack, repository/use case design, media service design, and navigation."
