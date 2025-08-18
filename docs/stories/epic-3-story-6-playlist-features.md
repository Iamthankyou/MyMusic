# Story 3.6 — Playlist Features (Epic 3: Discovery & Search)

## Status
- 📋 **DRAFT** - Playlist Features story created

## Story
As a music listener,
I want to browse trending playlists, view playlist details, and add tracks to playlists,
so that I can discover curated music collections and organize my favorite tracks.

## Scope
- Browse trending playlists from Jamendo API
- View playlist details with track listings
- Add tracks to playlists (local storage)
- Create and manage local playlists
- Play entire playlists
- Playlist artwork and metadata display

## Out of Scope
- Social playlist sharing
- Collaborative playlist editing
- Advanced playlist analytics
- Cross-device playlist sync

## References
- [Source: docs/prd.md#FR3]
- [Source: docs/fullstack-architecture.md#Domain-Layer]
- [Source: docs/stories/epic-3-discovery-search.md]

## Acceptance Criteria
1. User can browse trending playlists from Jamendo API
2. Playlist list shows artwork, title, track count, and creator
3. User can tap playlist to view detailed track listing
4. Playlist detail screen displays all tracks with artwork and metadata
5. User can play entire playlist or individual tracks
6. User can add tracks to local playlists
7. Local playlists are persisted in Room database
8. Playlist tracks maintain order and position
9. Loading and error states handled appropriately
10. UI follows existing Material 3 design patterns

## Tasks / Subtasks

### Phase 1: Data Layer Foundation
- **Task 1.1 (AC: 1, 2, 3)**: Create Playlist domain model
  - Create `domain/model/Playlist.kt` with id, title, description, artworkUrl, trackCount, creator
  - Create `domain/model/PlaylistTrack.kt` with playlistId, trackId, position
  - Create `domain/model/LocalPlaylist.kt` for user-created playlists

- **Task 1.2 (AC: 7, 8)**: Implement Playlist data layer
  - Create `data/local/PlaylistEntity.kt` and `PlaylistTrackEntity.kt`
  - Create `data/local/PlaylistDao.kt` with CRUD operations
  - Extend `AppDatabase.kt` with playlist tables
  - Create `data/mapper/PlaylistMapper.kt` for API ↔ Domain conversion

- **Task 1.3 (AC: 1)**: Add Jamendo playlist API integration
  - Create `data/remote/JamendoPlaylistsService.kt` with trending playlists endpoint
  - Add playlist endpoints to existing service or create new one
  - Implement playlist DTOs for API responses

### Phase 2: Domain Layer
- **Task 2.1 (AC: 1, 2, 3)**: Create playlist use cases
  - Create `domain/usecase/GetTrendingPlaylistsUseCase.kt`
  - Create `domain/usecase/GetPlaylistDetailUseCase.kt`
  - Create `domain/usecase/AddTrackToPlaylistUseCase.kt`
  - Create `domain/usecase/CreateLocalPlaylistUseCase.kt`

- **Task 2.2 (AC: 7, 8)**: Implement playlist repository
  - Create `domain/repository/PlaylistRepository.kt` interface
  - Create `data/repository/PlaylistRepositoryImpl.kt` implementation
  - Add repository to Hilt dependency injection

### Phase 3: UI Layer
- **Task 3.1 (AC: 1, 2)**: Create playlist browsing UI
  - Create `presentation/playlist/PlaylistListScreen.kt`
  - Create `presentation/playlist/PlaylistListViewModel.kt`
  - Create `presentation/playlist/components/PlaylistItem.kt`
  - Add playlist tab to bottom navigation

- **Task 3.2 (AC: 3, 4, 5)**: Implement playlist detail screen
  - Create `presentation/playlist/PlaylistDetailScreen.kt`
  - Create `presentation/playlist/PlaylistDetailViewModel.kt`
  - Create `presentation/playlist/components/PlaylistTrackItem.kt`
  - Integrate with existing PlaybackController

- **Task 3.3 (AC: 6, 7)**: Add local playlist management
  - Create `presentation/playlist/CreatePlaylistDialog.kt`
  - Create `presentation/playlist/AddToPlaylistDialog.kt`
  - Implement playlist creation and track addition flows

### Phase 4: Integration & Polish
- **Task 4.1 (AC: 5, 9)**: Integrate with playback system
  - Connect playlist tracks to PlaybackController
  - Implement playlist queue management
  - Add playlist shuffle and repeat options

- **Task 4.2 (AC: 10)**: UI/UX polish
  - Implement loading states and skeleton screens
  - Add error handling and empty states
  - Ensure accessibility compliance
  - Add smooth animations and transitions

## Definition of Done
- [ ] Trending playlists load from Jamendo API
- [ ] Playlist detail screens display correctly
- [ ] Local playlist creation and management works
- [ ] Tracks can be added to playlists
- [ ] Playlist playback integration complete
- [ ] UI follows Material 3 design patterns
- [ ] Loading and error states handled
- [ ] Room database integration working
- [ ] Unit tests for playlist logic
- [ ] UI tests for playlist flows

## Technical Notes
- Use existing Room database architecture for local playlists
- Follow existing MVVM pattern with ViewModels and Repositories
- Integrate with existing PlaybackController for audio playback
- Use Compose LazyColumn for playlist and track listings
- Implement proper state management with StateFlow
- Follow existing dependency injection patterns with Hilt

## Files to Create/Modify
- `domain/model/Playlist.kt` - New
- `domain/model/PlaylistTrack.kt` - New
- `domain/model/LocalPlaylist.kt` - New
- `domain/repository/PlaylistRepository.kt` - New
- `domain/usecase/GetTrendingPlaylistsUseCase.kt` - New
- `domain/usecase/GetPlaylistDetailUseCase.kt` - New
- `domain/usecase/AddTrackToPlaylistUseCase.kt` - New
- `domain/usecase/CreateLocalPlaylistUseCase.kt` - New
- `data/local/PlaylistEntity.kt` - New
- `data/local/PlaylistTrackEntity.kt` - New
- `data/local/PlaylistDao.kt` - New
- `data/mapper/PlaylistMapper.kt` - New
- `data/repository/PlaylistRepositoryImpl.kt` - New
- `data/remote/JamendoPlaylistsService.kt` - New
- `presentation/playlist/PlaylistListScreen.kt` - New
- `presentation/playlist/PlaylistListViewModel.kt` - New
- `presentation/playlist/PlaylistDetailScreen.kt` - New
- `presentation/playlist/PlaylistDetailViewModel.kt` - New
- `presentation/playlist/components/PlaylistItem.kt` - New
- `presentation/playlist/components/PlaylistTrackItem.kt` - New
- `presentation/playlist/CreatePlaylistDialog.kt` - New
- `presentation/playlist/AddToPlaylistDialog.kt` - New
- `presentation/navigation/BottomNav.kt` - Modify (add playlist tab)
- `presentation/navigation/NavGraph.kt` - Modify (add playlist routes)
- `di/RepositoryModule.kt` - Modify (add PlaylistRepository)
- `di/UseCaseModule.kt` - Modify (add playlist use cases)
- `AppDatabase.kt` - Modify (add playlist tables)

## Dependencies
- Jamendo API playlist endpoints
- Existing Room database setup
- Current PlaybackController integration
- Existing navigation structure
- Current UI theme and components

## Risk Assessment
- **Low Risk**: Playlist data models and local storage (standard CRUD)
- **Medium Risk**: Jamendo API playlist endpoint availability
- **Low Risk**: UI integration with existing components
- **Medium Risk**: Playlist playback queue management

## Estimated Effort
- **Phase 1**: 2-3 days (Data layer foundation)
- **Phase 2**: 1-2 days (Domain layer)
- **Phase 3**: 3-4 days (UI implementation)
- **Phase 4**: 2-3 days (Integration and polish)
- **Total**: 8-12 days

## Next Steps
1. Review and approve this story
2. Begin with Phase 1 (Data layer foundation)
3. Implement Jamendo API integration for playlists
4. Create local playlist storage with Room
5. Build UI components following existing patterns
6. Integrate with playback system
7. Add polish and testing
