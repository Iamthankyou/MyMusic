# Story 2.2 — Headset/Lockscreen/BT Controls & Advanced Audio Focus (Epic 2: Media Playback & Player UX)

## Status
- ✅ **COMPLETED** - Headset/Lockscreen/BT Controls & Audio Focus implemented

## Story
As a listener,
I want to control playback from headset buttons, lockscreen, and Bluetooth devices with proper audio focus handling,
so that playback behaves predictably across device integrations.

## Scope
- MediaSession actions mapping to play/pause/next/previous/seek.
- Handle headset/BT media button events and lockscreen controls.
- Advanced audio focus behavior (duck, pause/resume on transient loss) with `AudioAttributes`.

## Out of Scope
- Android Auto integration (separate story).

## References
- [Source: fullstack-architecture/8-playback-architecture-media3.md]
- [Source: fullstack-architecture/14-permissions-policies.md]

## Acceptance Criteria
1) Headset single-press toggles play/pause; double-press next (where supported).
2) Lockscreen shows controls and reflects state; actions work.
3) On transient audio focus loss: duck or pause, and resume afterwards.
4) On permanent loss: stop/hold state; no leaks or crashes.

## Tasks (Suggested Breakdown)
- MediaSession
  - Map actions to `PlaybackController` (`togglePlayPause`, `skipNext`, `skipPrevious`).
  - Ensure session metadata updates (title, artist, artwork).
- Audio Focus
  - Configure `AudioAttributes` and focus request; implement focus callbacks.
  - Implement ducking or pause depending on policy.
- Validation
  - Test with wired headset, BT headset, lockscreen.

## Definition of Done
- ✅ Controls work from headset/lockscreen/BT; audio focus behavior correct.
- ✅ UI and notification reflect playback state consistently.

## Implementation Summary
- ✅ **MediaSession Integration**: Complete MediaSessionService with proper session management
- ✅ **Audio Focus Management**: Advanced audio focus handling with ducking and pause/resume
- ✅ **Notification Controls**: Enhanced lockscreen notification with previous/next/play/pause controls
- ✅ **Metadata Updates**: Proper track metadata in MediaSession and notifications
- ✅ **Headset/BT Support**: Media button events properly handled through MediaSession
- ✅ **Audio Focus Policies**: Transient loss handling (duck/pause) and permanent loss handling

## Files Created/Modified
- `playback/MediaSessionService.kt` - New MediaSession service for external controls
- `playback/PlaybackController.kt` - Added audio focus management and metadata support
- `playback/PlaybackService.kt` - Enhanced notification with media controls
- `app/src/main/AndroidManifest.xml` - Added MediaSessionService declaration
- `app/build.gradle.kts` - Added androidx-media dependency
- `gradle/libs.versions.toml` - Added media library version

## Project Structure Notes
- Architecture docs under `docs/fullstack-architecture/*`.
