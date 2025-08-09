# MyMusic — Front-End Specification (UX/UI)

## Overview
- Purpose: Define UX/IA, key screens, interaction flows, and visual guidelines for MyMusic Android app.
- Sources: docs/prd.md (approved draft). Platform: Android, 100% Kotlin, Jetpack Compose.

## UX Vision
A modern, artwork-forward music discovery and listening experience. Fast, fluid lists; clear hierarchy; persistent mini-player for quick control; cohesive theming via Material 3 with dynamic color.

## Target Users & Goals
- Discover new music easily (Trending, Feeds, Explore).
- Seamless playback with minimal friction; quick access to Now Playing.
- Ability to download tracks for later listening.

## Information Architecture & Navigation
- Global navigation: Bottom Navigation with 5 items
  1) Trending
  2) Feeds
  3) Radio
  4) Stream
  5) Explore
- Section structure:
  - Trending: Tabs for Tracks | Albums | Playlists | Podcasts (if available)
  - Feeds: Curated/featured lists
  - Radio: Categories → Stations list → Play
  - Stream: Categories → Streams list → Play
  - Explore: Infinite scroll list
- Mini-player: Persistent across all sections at bottom; tap to expand Player.
- App Bar: Contextual title, optional actions (search later/out of scope MVP).

## Core Screens & Content
1) Trending
   - Layout: Top tabs (ScrollableTabRow), each tab shows a vertical list.
   - Card/List Item: Cover image (1:1), title, subtitle (artist/owner), optional meta (duration/count).
   - Actions: Primary tap to open detail or start playback (Tracks start playback; others open details).
   - States: Shimmer skeletons (Coil + placeholder), error retry, empty.

2) Feeds
   - Layout: Vertical feed of curated collections (horizontal carousels optional for later).
   - Item: Section header + horizontal list or large cards (MVP: vertical lists).
   - State handling as above.

3) Radio
   - Layout: Grid/list of categories; tapping shows stations list.
   - Station row: Logo/cover, name, bitrate (if available), play icon.
   - Tap station → starts playback and opens mini-player; full Player optional.

4) Stream
   - Similar to Radio; verify API distinction. MVP mirrors Radio patterns.

5) Explore
   - Layout: Full-width list with Paging 3 (infinite scroll).
   - Item: Mixed content or tracks list (MVP: tracks focus). Load more on end.

6) Details (Album/Playlist/Podcast)
   - Header: Large artwork, title, owner/artist, meta.
   - Actions: Play/Shuffle; context menu (later).
   - List of tracks/episodes with index, title, duration.

7) Player (Full Screen)
   - Artwork centered (square or 1:1.1), title, artist.
   - Controls: Play/Pause, Next, Previous, Seek bar with elapsed/remaining time.
   - Secondary actions: Repeat/Shuffle (later), Download button (for track).
   - Gesture: Swipe down/back to collapse to mini-player.

8) Mini-player
   - Compact bar above BottomNav: Artwork thumbnail, title, artist, Play/Pause, Next.
   - Tap expands to full Player; swipe dismiss pauses (optional).

9) Downloads
   - List of downloaded tracks with artwork, title, artist, file status.
   - Show progress rows for active downloads; cancel action.

## Components (Compose)
- AppScaffold: `Scaffold` with BottomNav, MiniPlayer slot, content.
- TopBar: `CenterAlignedTopAppBar` per screen.
- BottomNavigation: 5 destinations with icons + labels.
- TabRow: Trending sub-nav.
- List Items:
  - MediaListItem (track): artwork, title, subtitle, trailing actions.
  - MediaCard (album/playlist/podcast): square artwork, title, subtitle.
- PlayerControls: transport controls + seek bar.
- MiniPlayer: compact controls + thumbnail.
- EmptyState, ErrorState (retry), LoadingSkeletons (shimmer).
- DownloadRow: title, progress bar, actions.

## States & Behavior
- Loading: Shimmer placeholders; initial and page append loaders.
- Error: Inline error with retry; toast/snackbar for transient errors.
- Empty: Friendly illustration/title and CTA to explore.
- Offline: Banner indicator, reduced features (no streaming/download start).
- Permissions: Prompt flow for notifications (API 33+), foreground service rationale, storage access via `MediaStore` (no broad storage perms for 29+).

## Interaction Flows (MVP)
1) Browse → Play Track
   - User scrolls Trending Tracks → taps row → starts playback → mini-player appears → can expand to Player.
2) Expand/Collapse Player
   - Tap mini-player to open full Player; back/gesture collapses.
3) Seek & Transport
   - Drag seek bar; tap next/prev.
4) Download Track
   - From Player or track options → Download → show progress in Downloads → completion toast.
5) Radio/Stream Play
   - Tap station/stream → start playback; mini-player shows station title.

## Visual Design
- Color: Material 3 dynamic color support; fallback palette with dark/light themes.
- Typography: M3 defaults (Display/Headline/Title/Body/Label). Titles max 2 lines; subtitles 1 line ellipsized.
- Shapes/Elevation: M3 defaults; medium shape for cards; tonal surfaces for bars.
- Imagery: High-resolution artwork via Coil with memory/disk cache; placeholders and crossfade.
- Layout: 8dp base grid; comfortable spacing, 16dp screen margins, 12dp list item gaps.

## Accessibility
- Minimum 48x48dp touch targets; clear focus states.
- Content descriptions for artwork/buttons; speakable labels for controls.
- Contrast AA for text on surfaces; avoid text over busy images.
- Support TalkBack navigation for player controls and lists.

## Performance & Quality
- Use Paging 3 with `cachedIn(viewModelScope)`; prefetch distance 2–3.
- Limit image sizes; request thumbnail sizes where possible.
- Avoid excessive recomposition; remember derived state; use `LazyList` item keys.
- Handle process death: restore last playing item and position when possible.

## Telemetry (optional)
- Events: list_impression, item_tap, playback_start, playback_complete, download_start, download_complete, error.

## Open Questions
- Final distinction between Radio vs Stream in Jamendo API.
- Exact fields for Feeds; structure of Explore items.
- Branding assets (logo, custom palette) if any.

## Deliverables
- This spec (docs/front-end-spec.md).
- Wireframe annotations in text; high-fidelity visuals optional later.
