# Story 2.2 — Headset/Lockscreen/BT Controls & Advanced Audio Focus (Epic 2: Media Playback & Player UX)

## Status
- Draft

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
- Controls work from headset/lockscreen/BT; audio focus behavior correct.
- UI and notification reflect playback state consistently.

## Project Structure Notes
- Architecture docs under `docs/fullstack-architecture/*`.
