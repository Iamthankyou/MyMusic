# Story 1.4 — Mini-player Progress & Seek (Epic 1: Foundation & Trending Tracks MVP)

As a listener,
I want to see playback progress and seek from the mini-player,
so that I can quickly scrub within the current track without leaving the list.

## Status
- Draft

## Scope
- Extend playback state with `positionMs` and `durationMs`.
- Periodically update playback position while playing; pause stops position updates.
- Display a progress indicator in the mini-player; support user seek with a slider.
- Guard for unknown duration and disabled seek states.

## Out of Scope
- Full-screen Player UI (Now Playing) and queue management.
- Replay gain/volume boost, repeat/shuffle.
- Precision analytics/telemetry for scrubbing.

## References
- [Source: fullstack-architecture/8-playback-architecture-media3.md]
- [Source: fullstack-architecture/10-navigation-ui.md]
- [Source: fullstack-architecture/17-testing-strategy.md]
- [Source: fullstack-architecture/15-performance.md]

## Acceptance Criteria
1) Progress display
- Mini-player shows a progress component that advances while audio is playing and halts when paused.
- When duration is known, progress reflects the ratio `positionMs / durationMs`.
- When duration is unknown (TIME_UNSET), show an indeterminate progress (or hide slider).

2) Seek interaction
- Dragging the slider seeks to the chosen position and playback continues from there.
- Seeking is disabled when duration is unknown.

3) State consistency
- `positionMs` updates at a reasonable interval (e.g., 250–500ms) without UI jank.
- `durationMs` updates after player prepared; survives track switches.

4) Resilience
- No crashes on rapid taps, seeks, or when audioUrl is missing/null (seek disabled).
- Progress resets appropriately when a new track starts.

## Technical Notes
- Player: Media3 ExoPlayer provides `currentPosition` and `duration` (guard `C.TIME_UNSET`). [Source: fullstack-architecture/8-playback-architecture-media3.md]
- State: Extend `NowPlayingState(title, artist, isPlaying)` to include `positionMs`, `durationMs`. [Source: fullstack-architecture/8-playback-architecture-media3.md]
- UI: Use `Slider` for seek when duration known; fallback to `LinearProgressIndicator` or hidden when unknown. [Source: fullstack-architecture/10-navigation-ui.md]
- Performance: Use a single coroutine ticker (e.g., `delay(500L)`) on Main dispatcher; avoid excessive recompositions. [Source: fullstack-architecture/15-performance.md]
- Testing: Manual verification acceptable for MVP; automated tests can be added later. [Source: fullstack-architecture/17-testing-strategy.md]

## Tasks (Suggested Breakdown)
- Playback Controller
  - Extend `NowPlayingState` with `positionMs`, `durationMs`.
  - Add periodic position updater when `isPlaying = true`; stop when paused.
  - Expose `seekTo(positionMs: Long)`; update state after seeking.
- UI Integration
  - Update `MiniPlayer` to show progress; if duration known, show `Slider` with thumb and allow user scrubbing.
  - Disable slider when duration unknown; use indeterminate indicator or hide.
  - Show compact mm:ss current time and total duration (optional in this story).
- ViewModel
  - Provide pass-through methods `toggle()` and `seekTo()` to the controller.
- Polishing
  - Ensure state resets on new track; handle TIME_UNSET gracefully; clamp positions within [0, duration].

## Definition of Done
- Progress in mini-player advances while playing and stops when paused.
- Slider allows seeking when duration is known; disabled otherwise.
- Switching tracks resets progress and updates duration.
- App compiles and runs without crashes during play/seek interactions.

## Project Structure Notes
- Architecture docs are under `docs/fullstack-architecture/*`.


