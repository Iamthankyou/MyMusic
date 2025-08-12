# Story 2.5 — Android Auto Integration (Epic 2: Media Playback & Player UX)

## Status
- TBD

## Story
As a driver,
I want to control playback via Android Auto,
so that I can safely access my music in the car.

## Scope
- Integrate with Android Auto using Media3 session/browser if applicable.
- Provide minimal browse/play capabilities and metadata.

## Out of Scope
- Complex browse hierarchy; full-featured search.

## References
- [Source: fullstack-architecture/8-playback-architecture-media3.md]
- Google developer docs for Android Auto + Media3.

## Acceptance Criteria
1) App appears in Android Auto as a media source.
2) Basic controls (play/pause/next/prev) and metadata shown correctly.
3) Session stable; no crashes.

## Tasks (Suggested Breakdown)
- Session/Service
  - Evaluate `MediaLibraryService` if browse needed; otherwise enhance session to be Auto-friendly.
- Manifest & Metadata
  - Add required services/metadata for Android Auto.
- Validation
  - Test on emulator or device with Android Auto.

## Definition of Done
- Android Auto basic integration works; QA smoke OK.

## Project Structure Notes
- Architecture docs under `docs/fullstack-architecture/*`.
