# 5) Networking & API
- Base URL: `https://api.jamendo.com/v3.0/`
- Auth: query param `client_id=05c6ee3`
- Common params: `format=json`, paging via `limit` and `offset`
- HTTP client: OkHttp with logging interceptor (debug), timeout 20–30s
- Serialization: Kotlinx Serialization (Json { ignoreUnknownKeys = true })
- Error handling: map HTTP/network errors to domain `AppError`

Retrofit service interfaces (examples):
- `JamendoTracksService`: `GET /tracks`
- `JamendoAlbumsService`: `GET /albums`
- `JamendoPlaylistsService`: `GET /playlists`
- `JamendoRadiosService`: `GET /radios`
- `JamendoPodcastsService`: `GET /podcasts` (verify availability)
- `JamendoFeedsService`: `GET /feeds` or appropriate endpoint (verify)

DTO → Domain mappers ensure presentation is decoupled from API shapes.
