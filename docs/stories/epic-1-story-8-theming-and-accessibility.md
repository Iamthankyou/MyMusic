# Story 1.8 — Theming (M3, Dark Mode) & Accessibility Baseline (Epic 1)

## Status
- Draft

## Story
As a user,
I want consistent theming and accessible UI,
so that the app looks polished and is usable by everyone.

## Scope
- Material 3 theme baseline: color scheme (dynamic color if available), typography, shapes.
- Dark mode support and contrast checks for key surfaces/text.
- Accessibility: content descriptions, minimum touch targets, focus order.

## Out of Scope
- Full localization and RTL.
- Detailed motion/animation polish (later epics).

## References
- [Source: fullstack-architecture/10-navigation-ui.md]
- [Source: front-end-spec/accessibility.md]

## Acceptance Criteria
1) App theme uses M3 with dark mode and dynamic color (Android 12+).
2) Key UI surfaces and text meet contrast guidelines.
3) Images and controls have proper `contentDescription`; tap targets ≥ 48dp.
4) Basic keyboard/focus traversal is sensible.

## Tasks (Suggested Breakdown)
- Theme
  - Configure Material 3 theme, dynamic color, dark mode preview.
- A11y
  - Audit current screens; add content descriptions and semantics.
  - Ensure control sizes meet minimum targets.
- Validation
  - Manual contrast and TalkBack smoke checks.

## Definition of Done
- App visually coherent in light/dark; a11y baseline complete.

## Project Structure Notes
- Architecture docs under `docs/fullstack-architecture/*`.
