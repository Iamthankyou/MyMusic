# Story 2.4 — Playback Speed, Repeat & Shuffle (Epic 2: Media Playback & Player UX)

## Status
- ✅ **COMPLETED** - Playback Speed, Repeat & Shuffle implemented

## Story
As a listener,
I want to change playback speed and use repeat/shuffle modes,
so that I can personalize how I listen.

## Scope
- Controls for speed (e.g., 0.5x–2.0x with common steps), repeat (off/one/all), shuffle on/off.
- Expose state in `NowPlayingState` or dedicated settings state.

## Out of Scope
- Per-track speed memory.

## References
- [Source: fullstack-architecture/8-playback-architecture-media3.md]

## Acceptance Criteria
1) User can change speed; it affects playback immediately and is reflected in UI.
2) Repeat and shuffle work; icons/states update.
3) Settings persisted for next session (optional).

## Tasks (Suggested Breakdown)
- Controller
  - Add `setSpeed()`, `setRepeatMode()`, `toggleShuffle()` to `PlaybackController`.
  - Persist preferred speed/modes via DataStore (optional).
- UI & Now Playing
  - Add controls in Now Playing; show current modes.

## Definition of Done
- ✅ Speed/repeat/shuffle functional; state updates reflected everywhere.

## Implementation Summary
- ✅ **Playback Speed Control**: Added speed control with range 0.25x-2.0x, accessible via dialog
- ✅ **Repeat Mode Control**: Implemented repeat modes (off/one/all) with visual indicators
- ✅ **Shuffle Mode Control**: Added shuffle toggle with visual feedback
- ✅ **UI Integration**: Added controls to PlayerRoute with proper state management
- ✅ **MiniPlayer Updates**: Added visual indicators for active modes in mini player
- ✅ **State Management**: All controls properly integrated with PlaybackController state

## Files Created/Modified
- `playback/PlaybackController.kt` - Added speed, repeat, shuffle controls and state management
- `presentation/player/PlayerViewModel.kt` - Exposed new controls to UI layer
- `presentation/player/PlayerRoute.kt` - Added UI controls for speed, repeat, shuffle with SpeedDialog
- `presentation/player/MiniPlayer.kt` - Added visual indicators for active modes

## Project Structure Notes
- Architecture docs under `docs/fullstack-architecture/*`.
