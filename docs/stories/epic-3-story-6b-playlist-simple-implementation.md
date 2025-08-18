# Story 3.6b — Simple Playlist Implementation (Epic 3: Discovery & Search)

## Status
- 📋 **DRAFT** - Simple Playlist Implementation story created

## Story
As a music listener,
I want to browse trending playlists and view their tracks,
so that I can discover curated music collections and play them.

## Scope
- Browse trending playlists from Jamendo API
- View playlist details with track listings
- Play playlist tracks using existing playback system
- Simple playlist artwork and metadata display

## Out of Scope
- Local playlist creation/management
- Adding tracks to playlists
- Complex playlist operations
- Advanced UI features

## References
- [Source: docs/prd.md#FR3]
- [Source: docs/fullstack-architecture.md#Domain-Layer]
- [Source: docs/stories/epic-3-discovery-search.md]

## Acceptance Criteria
1. User can browse trending playlists from Jamendo API
2. Playlist list shows artwork, title, and track count
3. User can tap playlist to view detailed track listing
4. Playlist detail screen displays all tracks with basic info
5. User can play individual tracks from playlist
6. Loading and error states handled appropriately
7. UI follows existing Material 3 design patterns

## Single Phase Implementation

### **Phase 1: Complete Playlist Feature (4-6 hours)**

#### **Data Layer (1-2 hours)**
- Create `domain/model/Playlist.kt` with id, title, artworkUrl, trackCount
- Create `data/remote/JamendoPlaylistsService.kt` with trending playlists endpoint
- Create `data/mapper/PlaylistMapper.kt` for API conversion
- Create `domain/repository/PlaylistRepository.kt` interface
- Create `data/repository/PlaylistRepositoryImpl.kt` implementation

#### **Domain Layer (1 hour)**
- Create `domain/usecase/GetTrendingPlaylistsUseCase.kt`
- Create `domain/usecase/GetPlaylistDetailUseCase.kt`
- Add to Hilt DI modules

#### **UI Layer (2-3 hours)**
- Create `presentation/playlist/PlaylistListScreen.kt`
- Create `presentation/playlist/PlaylistListViewModel.kt`
- Create `presentation/playlist/components/PlaylistItem.kt`
- Create `presentation/playlist/PlaylistDetailScreen.kt`
- Create `presentation/playlist/PlaylistDetailViewModel.kt`
- Create `presentation/playlist/components/PlaylistTrackItem.kt`
- Add playlist tab to bottom navigation
- Add playlist routes to NavGraph

## Technical Notes
- **Integration Approach:** Follow existing Track/Album patterns exactly
- **Existing Pattern Reference:** Copy structure from `TrackItem.kt` and `TrackMapper.kt`
- **Key Constraints:** Use existing PlaybackController, no database changes needed
- **API Integration:** Add to existing JamendoTracksService or create simple new service

## Files to Create/Modify
- `domain/model/Playlist.kt` - New (copy Track.kt structure)
- `data/remote/JamendoPlaylistsService.kt` - New (simple API service)
- `data/mapper/PlaylistMapper.kt` - New (copy TrackMapper.kt pattern)
- `domain/repository/PlaylistRepository.kt` - New (copy TrackRepository.kt pattern)
- `data/repository/PlaylistRepositoryImpl.kt` - New (copy TrackRepositoryImpl.kt pattern)
- `domain/usecase/GetTrendingPlaylistsUseCase.kt` - New (copy existing use case pattern)
- `domain/usecase/GetPlaylistDetailUseCase.kt` - New (copy existing use case pattern)
- `presentation/playlist/PlaylistListScreen.kt` - New (copy DiscoveryScreen.kt structure)
- `presentation/playlist/PlaylistListViewModel.kt` - New (copy DiscoveryViewModel.kt structure)
- `presentation/playlist/components/PlaylistItem.kt` - New (copy TrackItem.kt structure)
- `presentation/playlist/PlaylistDetailScreen.kt` - New (copy existing detail screen pattern)
- `presentation/playlist/PlaylistDetailViewModel.kt` - New (copy existing ViewModel pattern)
- `presentation/playlist/components/PlaylistTrackItem.kt` - New (copy TrackItem.kt structure)
- `presentation/navigation/BottomNav.kt` - Modify (add playlist tab)
- `presentation/navigation/NavGraph.kt` - Modify (add playlist routes)
- `di/RepositoryModule.kt` - Modify (add PlaylistRepository)
- `di/UseCaseModule.kt` - Modify (add playlist use cases)

## Dependencies
- Jamendo API playlist endpoints (verify availability)
- Existing PlaybackController integration
- Current navigation structure
- Existing UI theme and components

## Risk Assessment
- **Low Risk**: Following existing patterns exactly
- **Medium Risk**: Jamendo API playlist endpoint availability
- **Low Risk**: UI integration with existing components

## Definition of Done
- [ ] Trending playlists load from Jamendo API
- [ ] Playlist list displays correctly
- [ ] Playlist detail screens work
- [ ] Track playback integration works
- [ ] UI follows existing Material 3 patterns
- [ ] Loading and error states handled
- [ ] Navigation integration complete
- [ ] Basic functionality tested

## Implementation Strategy
1. **Copy existing patterns** - Use Track/Album code as templates
2. **Minimal changes** - Only add what's absolutely necessary
3. **Reuse existing components** - Adapt TrackItem for PlaylistItem
4. **Simple API integration** - Basic GET requests only
5. **Quick UI implementation** - Copy existing screen structures

## Success Metrics
- **Development Time**: 4-6 hours maximum
- **Functionality**: Basic playlist browsing and playback
- **Integration**: Seamless with existing system
- **Quality**: Follows existing code standards

## Next Steps
1. Verify Jamendo playlist API endpoints
2. Start with data layer (copy existing patterns)
3. Implement UI layer (copy existing screen structures)
4. Add navigation integration
5. Test basic functionality
6. Polish if time permits

## Notes
- This is a **MINIMAL VIABLE IMPLEMENTATION**
- Focus on **WORKING FUNCTIONALITY** over perfect design
- **COPY EXISTING PATTERNS** rather than inventing new ones
- **KEEP IT SIMPLE** - no advanced features in this phase
