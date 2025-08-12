# Story 2.3 — Persist Playback State (Epic 2: Media Playback & Player UX)

## Status
- TBD

## Story
As a listener,
I want the app to remember the current track and position across app restarts,
so that I can continue where I left off.

## Scope
- Persist current track id/metadata and position periodically.
- Restore playback state on app launch; optional auto-resume (setting).

## Out of Scope
- Full queue persistence (basic last queue only if trivial).

## References
- [Source: fullstack-architecture/8-playback-architecture-media3.md]
- [Source: fullstack-architecture/7-data-layer.md]

## Acceptance Criteria
1) After process death/restart, app shows last track metadata in mini-player.
2) Optional: resume playback automatically based on a setting; otherwise ready-to-play at remembered position.
3) No data loss on frequent updates (throttled writes).

## Tasks (Suggested Breakdown)
- Storage
  - Choose persistence (DataStore/Room). MVP: DataStore for last track + position.
  - Periodic save (e.g., every 5s or onPause/stop) from `PlaybackController`.
- Restore
  - On app start, load state into `PlaybackController`/`PlayerViewModel`.
  - If auto-resume enabled, seek and start playback.
- Settings (optional)
  - Toggle for auto-resume.

## Definition of Done
- Last track & position restored; behavior matches setting.
- No UI crash; battery-friendly persistence cadence.

## Project Structure Notes
- Architecture docs under `docs/fullstack-architecture/*`.
