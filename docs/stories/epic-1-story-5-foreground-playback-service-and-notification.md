# Story 1.5 — Foreground Playback Service, MediaSession & Notification (Epic 1)

## Status
- Done

## Story
As a listener,
I want playback to continue in background with notification controls,
so that I can control audio when the app is not in the foreground.

## Scope
- Implement `MediaSessionService`-based foreground playback service.
- Create `MediaSession` attached to the existing ExoPlayer instance.
- Show persistent media notification with play/pause and stop actions.
- Handle audio focus and media attributes (usage=media, contentType=music).
- Wire lifecycle: start/stop service appropriately.

## Out of Scope
- Full queue management UI (kept minimal for MVP).
- Android Auto / MediaLibrary browsing.

## References
- [Source: fullstack-architecture/8-playback-architecture-media3.md]
- [Source: fullstack-architecture/14-permissions-policies.md]
- [Source: fullstack-architecture/18-build-release.md]
- [Source: fullstack-architecture/11-di-graph-hilt.md]

## Acceptance Criteria
1) Foreground service runs during playback and stops when playback ends/stopped.
2) Notification shows current track and exposes play/pause (and stop) actions.
3) MediaSession integrates with system (lockscreen/headset) for play/pause.
4) Audio focus is handled correctly (ducking/pause as appropriate).
5) App does not crash across start/stop cycles and process death.

## Tasks (Suggested Breakdown)
- Service & Session
  - Create `PlaybackService : MediaSessionService`.
  - Create and manage `MediaSession` bound to ExoPlayer.
  - Expose session token per Media3 requirements.
- Notification & Foreground
  - Configure notification channel, show persistent notification.
  - Start foreground with notification on playback; stop when idle.
- Audio Attributes & Focus
  - Set audio attributes on player; verify focus callbacks behavior.
- Integration
  - Update DI to provide session/service dependencies as needed.
  - Manifest: service declaration, foreground permission.
- Validation
  - Manual checks: screen off, app backgrounded, headset controls.

## Definition of Done
- Background playback stable; notification controls work.
- Service lifecycle correct; no leaks/crashes.
- Manifest and permissions configured.

## Project Structure Notes
- Architecture docs under `docs/fullstack-architecture/*`.
