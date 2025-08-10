# Story 1.3 — Tap-to-Play & Mini-player (Epic 1: Foundation & Trending Tracks MVP)

As a listener,
I want to tap any track in Trending to start playback and control it via a mini-player,
so that I can quickly listen and pause/resume without leaving the list.

## Status
- Done

## Scope
- Integrate Media3 ExoPlayer via a `PlaybackController` (Hilt `@Singleton`).
- Wire `TrendingScreen` item tap to trigger playback using track `audioUrl`.
- Add a `MiniPlayer` composable anchored at the bottom to reflect state (title, artist, play/pause).
- Create simple `PlayerViewModel` or expose state from `PlaybackController` for Compose.

## Out of Scope
- Background playback with foreground service and notification controls (planned later in Epic 1).
- Full player screen (Now Playing) and queue management.
- Downloads.

## References
- [Source: fullstack-architecture/8-playback-architecture-media3.md]
- [Source: fullstack-architecture/10-navigation-ui.md]
- [Source: fullstack-architecture/17-testing-strategy.md]

## Acceptance Criteria
1) Tap-to-Play
- Tapping a row in Trending starts playback of that track's `audioUrl` using Media3 ExoPlayer.
- If a different track is already playing, it switches to the new one.

2) Mini-player UI
- A mini-player bar is visible at the bottom of the app when a track is loaded.
- Mini-player shows artwork (if available), title, and artist of the current track.
- Play/Pause button toggles playback state; UI reflects the current state.

3) State & Lifecycle
- Playback logic encapsulated in a controller exposed via Hilt `@Singleton`.
- UI observes state (isPlaying, title, artist) via Flow/StateFlow and updates accordingly.

4) Quality
- No crashes when tapping rapidly between items.
- Basic unit tests covering controller start/stop calls (use fakes where applicable) and UI mapping logic where feasible.

## Technical Notes
- Player: Media3 ExoPlayer singleton via Hilt. Session/service and notification are recommended by architecture but deferred to a later story to keep this vertical slice small. [Source: fullstack-architecture/8-playback-architecture-media3.md]
- UI integration: Expose a small immutable state for mini-player (e.g., `NowPlayingState(title, artist, isPlaying)`). [Source: fullstack-architecture/8-playback-architecture-media3.md]
- Scaffold: Mini-player is anchored at bottom of the app content; BottomNav comes later. [Source: fullstack-architecture/10-navigation-ui.md]
- Testing: Add simple unit tests per testing strategy; UI tests can be added later when end-to-end flows stabilize. [Source: fullstack-architecture/17-testing-strategy.md]

## Tasks (Suggested Breakdown)
- Playback Controller
  - Create `PlaybackController` wrapping ExoPlayer with `play(url)`, `pause()`, `toggle()`, `observeState()`.
  - Provide Hilt module to expose controller as `@Singleton`.
- UI Integration
  - Update `TrendingScreen` to call controller `play(url)` on item tap (only when `audioUrl` is non-null).
  - Create `MiniPlayer` composable and place it at the bottom (e.g., using a `Scaffold`).
  - Bind mini-player to controller state; implement play/pause control.
- ViewModel
  - Add `PlayerViewModel` if needed to bridge controller state to Compose.
- Tests
  - Unit test: controller state mapping and next calls using a fake player.
  - Optional UI test: verify mini-player reacts to state (smoke test).

## Definition of Done
- All Acceptance Criteria met.
- Code compiles and runs; mini-player appears and controls playback for tapped items.
- Minimal unit tests added and passing.
- Code formatted and lints cleanly.

## Project Structure Notes
- Architecture docs are currently under `docs/fullstack-architecture/*`, while core-config references `docs/architecture/*`. For this story, references point to `docs/fullstack-architecture/*`.


## Tasks / Subtasks
- [x] Playback Controller
  - [x] Tạo `PlaybackController` (Media3 ExoPlayer) với `play`, `pause`, `toggle`, exposed state
- [x] UI Integration
  - [x] `TrendingScreen` gọi `onTrackClicked` → `controller.play(...)`
  - [x] Thêm `MiniPlayer` ở `Scaffold.bottomBar`
- [x] ViewModel
  - [x] Thêm `PlayerViewModel` expose `NowPlayingState`

### Completion Notes List
- Implement `PlaybackController` singleton và state flow
- Hook tap-to-play từ `TrendingScreen` qua `TrendingViewModel`
- Thêm `MiniPlayer` và Scaffold integration ở `MainActivity`

### File List
- Added: `app/src/main/java/com/example/mymusic/playback/NowPlayingState.kt`
- Added: `app/src/main/java/com/example/mymusic/playback/PlaybackController.kt`
- Added: `app/src/main/java/com/example/mymusic/presentation/player/PlayerViewModel.kt`
- Added: `app/src/main/java/com/example/mymusic/presentation/player/MiniPlayer.kt`
- Modified: `app/src/main/java/com/example/mymusic/presentation/home/trending/TrendingScreen.kt`
- Modified: `app/src/main/java/com/example/mymusic/presentation/home/trending/TrendingViewModel.kt`
- Modified: `app/src/main/java/com/example/mymusic/MainActivity.kt`