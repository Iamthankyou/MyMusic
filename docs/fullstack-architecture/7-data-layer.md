# 7) Data Layer
- Remote: Retrofit services + request builders injecting `client_id` and defaults via Interceptor
- Local: Optional caching (Room) for lists or last playback state (MVP: in-memory)
- Repository Implementations: Combine remote/local; expose `Flow`/`PagingData`
- Paging: `PagingSource`/`RemoteMediator` (start with simple `PagingSource` using `offset`/`limit`)

Paging Sources (examples):
- `TrendingTracksPagingSource`
- `TrendingAlbumsPagingSource`
- `ExplorePagingSource`
