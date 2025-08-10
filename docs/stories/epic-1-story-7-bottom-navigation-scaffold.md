# Story 1.7 — Bottom Navigation Scaffold (Epic 1)

## Status
- Draft

## Story
As a user,
I want bottom navigation to switch between core sections,
so that I can access Trending and future screens quickly.

## Scope
- Introduce `Scaffold` with BottomNav and stable routes.
- Tabs (initial): `Trending`, `Explore (placeholder)`, `Downloads (placeholder)`.
- Preserve state per tab when switching.

## Out of Scope
- Deep links and back-stack restoration across process death (later).
- Full Explore/Downloads features (placeholders only).

## References
- [Source: fullstack-architecture/10-navigation-ui.md]

## Acceptance Criteria
1) BottomNav with icons/labels; active tab highlighted.
2) Start destination is `Trending`; switching tabs preserves list scroll state.
3) Mini-player remains anchored above BottomNav and visible across tabs.

## Tasks (Suggested Breakdown)
- Navigation
  - Define route constants and NavHost with tabs.
  - Add placeholder composables for Explore/Downloads.
- UI
  - Implement BottomNav with Material 3.
  - Ensure mini-player sits above BottomNav.
- State
  - Preserve scroll position per tab.

## Definition of Done
- Bottom navigation works with preserved state and integrated mini-player.

## Project Structure Notes
- Architecture docs under `docs/fullstack-architecture/*`.
