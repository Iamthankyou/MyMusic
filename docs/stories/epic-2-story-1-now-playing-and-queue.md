# Story 2.1 — Now Playing Screen & Queue (Epic 2: Media Playback & Player UX)

## Status
- ✅ **COMPLETED** - Now Playing screen and queue functionality implemented

## Story
As a listener,
I want a full Now Playing screen and a queue with next/previous controls,
so that I can see details and control playback beyond the mini-player.

## Scope
- Now Playing screen (artwork, title, artist, elapsed/remaining, play/pause, next/prev).
- Queue model and UI (simple list of upcoming/previous items) with tap-to-jump.
- Bind to `PlaybackController` and future `MediaSession` events.

## Out of Scope
- Advanced queue management (reorder, remove) — later story.
- Lyrics, visualizer.

## References
- [Source: fullstack-architecture/8-playback-architecture-media3.md]
- [Source: fullstack-architecture/10-navigation-ui.md]
- [Source: fullstack-architecture/17-testing-strategy.md]

## Acceptance Criteria
1) Now Playing shows metadata and controls; reflects playing/paused state.
2) Next/Prev buttons work and update UI state.
3) Queue visible and can jump to item; current item highlighted.
4) State survives configuration changes; no crashes on rapid interactions.

## Tasks (Suggested Breakdown)
- UI
  - Create `PlayerRoute` composable for Now Playing screen.
  - Show artwork, title, artist, elapsed/duration, play/pause, next/prev.
  - Add a queue list (simple vertical list) with tap-to-jump.
- Playback Integration
  - Extend `PlaybackController` with queue operations (`setQueue(list)`, `skipNext()`, `skipPrevious()`, `playAt(index)`).
  - Expose queue and current index in state or as separate flows.
- Navigation
  - Add `PlayerRoute` to NavHost and entry-point from mini-player tap.
- Polishing
  - Handle unknown duration gracefully; clamp positions; UI loading states.

## Definition of Done
- ✅ Now Playing and queue functional; next/prev works.
- ✅ App compiles and runs; UI reflects playback state consistently.

## Implementation Summary
- ✅ **PlayerRoute Composable**: Complete Now Playing screen with artwork, metadata, controls
- ✅ **Queue Management**: Queue model with tap-to-jump functionality and current item highlighting
- ✅ **PlaybackController Extensions**: Added queue operations (setQueue, skipNext, skipPrevious, playAt)
- ✅ **Navigation Integration**: PlayerRoute added to NavHost with entry point from MiniPlayer
- ✅ **State Management**: Queue and current index exposed as StateFlows
- ✅ **UI Polish**: Proper loading states, error handling, and graceful duration handling
- ✅ **Queue with Real Tracks**: Fixed to use actual trending tracks instead of sample tracks
- ✅ **MiniPlayer Visibility**: Fixed overlap issue by hiding MiniPlayer in PlayerRoute
- ✅ **Next/Previous Navigation**: Fixed to properly navigate through real tracks in queue

## Files Created/Modified
- `presentation/player/PlayerRoute.kt` - Complete Now Playing screen implementation
- `playback/PlaybackController.kt` - Added queue operations and state management
- `presentation/player/PlayerViewModel.kt` - Exposed PlaybackController for UI access
- `presentation/player/MiniPlayer.kt` - Added navigation to PlayerRoute on tap
- `MainActivity.kt` - Added PlayerRoute to NavHost, fixed MiniPlayer visibility
- `presentation/home/trending/TrendingViewModel.kt` - Set queue with real tracks from trending list
- `presentation/home/trending/TrendingScreen.kt` - Added loaded tracks tracking for queue management

## Project Structure Notes
- Architecture docs under `docs/fullstack-architecture/*`.
