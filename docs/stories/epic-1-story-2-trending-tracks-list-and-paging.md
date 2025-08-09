# Story 1.2 — Trending Tracks List with Paging (Epic 1: Foundation & Trending Tracks MVP)

As a listener,
I want to browse a list of trending tracks with smooth infinite scrolling,
so that I can quickly discover new music and start playback from any item.

## Scope
- Implement Trending Tracks screen using Jetpack Compose and Paging 3
- Integrate with `GetTrendingTracksUseCase` (temporary list API for now)
- Build `PagingSource` powered by `JamendoTracksService` (offset/limit, order=popularity_total)
- Display rows: artwork, title, artist, duration
- Handle load states: initial loading, append loading, error, empty
- Wire Navigation from `MainActivity` scaffold to this screen

## Out of Scope
- Player controls and media service (future story)
- Detail pages (album/playlist/podcast)
- Downloads

## References
- Architecture: `docs/fullstack-architecture/`
- PRD (FR1, FR16): `docs/prd/`
- Front-End Spec: `docs/front-end-spec/`
- API: `GET /tracks?order=popularity_total&limit&offset&client_id&format=json`

## Acceptance Criteria
1) Paging
- Uses `PagingSource` or `Pager(flow)` with `PagingConfig(pageSize=20, prefetchDistance=2)`
- Uses `cachedIn(viewModelScope)` in ViewModel
- Appends pages on scroll to end

2) UI
- `LazyColumn` showing each track row: square artwork (Coil), title, artist, duration mm:ss
- Initial loading state shows placeholders/shimmer or progress
- Append loading indicator at list bottom
- Error state with retry button (for both initial and append errors)
- Empty state (if zero items)

3) Integration
- ViewModel calls `Pager` that delegates to repository via a `PagingSource` using `JamendoTracksService`
- Pulls `order=popularity_total`, `format=json`, `client_id` automatically via interceptor
- Gracefully handles missing fields in DTO

4) Navigation
- Add a BottomNav or single-route scaffold with `Trending` destination as start
- App launches to Trending screen and displays data

5) Build & Quality
- Build succeeds; no runtime crashes when scrolling/refreshing
- Basic unit test(s) for `PagingSource` mapping and next key logic given fake DTOs

## Technical Notes
- Create `TrendingTracksPagingSource` that maps `limit/offset` to service calls; `nextKey = offset + limit` until results < limit
- Create `TrendingViewModel` exposing `Flow<PagingData<TrackUiModel>>`
- Introduce `TrackUiModel` if formatting or presentation differs from domain
- Time formatting helper for mm:ss
- Use `rememberAsyncImagePainter` (Coil) or `SubcomposeAsyncImage`

## Tasks
- Create `data/paging/TrendingTracksPagingSource.kt`
- Create `presentation/home/trending/TrendingViewModel.kt`
- Create `presentation/home/trending/TrendingScreen.kt` (Composable list + load state handling)
- Add Navigation route and set as start destination
- Add simple unit tests for `TrendingTracksPagingSource` (happy path, empty page)

## Definition of Done
- All acceptance criteria met
- Code compiles and runs; list loads and paginates
- Load/error/empty states visible and functional
- Minimal tests for paging source
- File List updated in story dev record when implemented
