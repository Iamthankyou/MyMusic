# Story 1.6 — Loading/Empty/Error Polish & Unified Retry UX (Epic 1)

## Status
- Draft

## Story
As a user,
I want consistent loading, empty, and error states across lists,
so that the app feels polished and predictable.

## Scope
- Create reusable Compose components for loading, empty, and error with retry.
- Apply consistent visuals (icons, copy, spacing) across Trending and future screens.
- Centralize retry callbacks and error mapping (network vs no-results).

## Out of Scope
- Telemetry for errors (future optional epic).
- Localization copy polish.

## References
- [Source: fullstack-architecture/12-error-handling-resilience.md]
- [Source: fullstack-architecture/10-navigation-ui.md]
- [Source: fullstack-architecture/15-performance.md]

## Acceptance Criteria
1) Unified components: `AppLoading`, `AppEmpty`, `AppError` with retry.
2) Trending screen uses these components instead of ad-hoc UI.
3) Error copy differentiates between network error and API-empty.
4) Visuals match Material 3 and work in dark mode.

## Tasks (Suggested Breakdown)
- Components
  - Implement `AppLoading()`, `AppEmpty(onRetry)`, `AppError(message, onRetry)`.
  - Add basic icons and spacing tokens.
- Integration
  - Replace current load/error/empty in `TrendingScreen` with new components.
- Error Mapping
  - Map common exceptions to user-friendly messages.
- Polish
  - Ensure a11y: content descriptions, tap targets, focus order.

## Definition of Done
- Components reusable and integrated in Trending.
- Retry works and error messages are clear.

## Project Structure Notes
- Architecture docs under `docs/fullstack-architecture/*`.
