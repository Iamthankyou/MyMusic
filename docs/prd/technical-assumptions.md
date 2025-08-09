# Technical Assumptions

## Repository Structure
Monorepo (single Android project). Start with one `app` module; consider splitting `data`/`domain` later.

## Service Architecture
Monolithic Android app with Clean Architecture layers enforced via packages/modules.

## Testing Requirements
Unit + Integration where practical (e.g., ViewModel/unit, repository/integration with fake servers). UI testing added progressively.

## Additional Technical Assumptions and Requests
- Retrofit + OkHttp logging interceptor; Kotlinx Serialization.
- Paging 3 with `PagingSource` and `cachedIn(viewModelScope)`.
- Media3 ExoPlayer, MediaSession, foreground service notification.
- Coil for images; Accompanist Permissions if needed.
- DownloadManager + MediaStore for downloads; WorkManager optional for long-running tasks.
- Compose Navigation for in-app routing; material icons.
