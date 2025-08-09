# 6) Domain Layer
Entities (examples):
- `Track(id, title, artist, albumId, durationMs, artworkUrl, audioUrl, isDownloadable)`
- `Album(id, title, artist, trackCount, artworkUrl)`
- `Playlist(id, title, trackCount, artworkUrl)`
- `Podcast(id, title, episodeCount, artworkUrl)` (if supported)
- `Radio(id, name, streamUrl, artworkUrl)`

Repository Contracts:
- `TrackRepository` (trending, detail)
- `AlbumRepository`
- `PlaylistRepository`
- `PodcastRepository` (optional)
- `RadioRepository`
- `ExploreRepository`
- `DownloadRepository`

Use Cases:
- `GetTrendingTracksUseCase`, `GetTrendingAlbumsUseCase`, `GetTrendingPlaylistsUseCase`, `GetTrendingPodcastsUseCase`
- `GetTopFeedsUseCase`
- `GetRadioCategoriesUseCase`, `GetStreamCategoriesUseCase`
- `GetExplorePagingUseCase`
- `GetTrackDetailUseCase`
- `StartPlaybackUseCase`, `TogglePlayPauseUseCase`, `SeekUseCase`, `SkipNextUseCase`, `SkipPrevUseCase`
- `DownloadTrackUseCase`, `ObserveDownloadsUseCase`
