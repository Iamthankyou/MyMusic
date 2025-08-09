# Story 1.1 — Foundation Setup & Network Client (Epic 1: Foundation & Trending Tracks MVP)

As a developer,
I want the Android project to be fully configured with core dependencies, DI, and a Jamendo API client,
so that subsequent stories can build vertical slices (e.g., Trending Tracks) quickly and safely.

## Scope
- Gradle configuration for Kotlin/Compose/Hilt/Retrofit/Paging/Media3/Coil
- Hilt application setup and DI modules
- Retrofit + OkHttp + Kotlinx Serialization JSON configuration
- BuildConfig `JAMENDO_CLIENT_ID` wiring from properties
- Define base Jamendo service(s) and DTOs for Tracks (for later use)
- Domain models, repository contract for Tracks, and initial mapper
- Navigation scaffold and base theme setup

## Out of Scope
- Implementing Trending Tracks UI or Paging (next story)
- Media playback service, downloads, or detailed screens

## References
- PRD: `docs/prd/`
- Front-End Spec: `docs/front-end-spec/`
- Architecture: `docs/fullstack-architecture/`
- API: `https://api.jamendo.com/v3.0` with `client_id=05c6ee3`

## Acceptance Criteria
1) Gradle and Plugins
- app/build.gradle.kts uses Compose BOM and enables Compose
- Add dependencies: Material3, Navigation Compose, Hilt (plugin + runtime + kapt), Retrofit, OkHttp (logging), Kotlinx Serialization JSON, Paging (runtime + Compose), Coil, Media3 (exoplayer, session)
- Kotlinx Serialization plugin applied
- Proguard rules placeholder updated if needed for Retrofit/Serialization/Media3

2) BuildConfig & Secrets
- `gradle.properties` (or `local.properties`) contains `JAMENDO_CLIENT_ID=05c6ee3` (placeholder OK)
- `BuildConfig.JAMENDO_CLIENT_ID` exposed to app via BuildConfig field

3) Hilt Setup
- `MyMusicApp` class annotated with `@HiltAndroidApp`
- `AndroidManifest.xml` uses `android:name=".MyMusicApp"`

4) DI Modules (under `com.example.mymusic.di`)
- `NetworkModule`: provides OkHttpClient (with logging in debug), Kotlinx Json, Retrofit (baseUrl `https://api.jamendo.com/v3.0/`), and Track service
- `RepositoryModule`: binds `TrackRepository` to `TrackRepositoryImpl`
- `UseCaseModule`: provides `GetTrendingTracksUseCase` (stub implementation calling repository)
- `ImageModule`: provides Coil `ImageLoader`

5) API Layer (under `com.example.mymusic.data.remote`)
- `JamendoTracksService` with `GET /tracks` supporting `client_id`, `format=json`, `limit`, `offset`, and ordering (e.g., `order=popularity_total`) as query params
- DTOs for tracks minimal fields: id, name/title, artist_name, duration, image/album image, audio URL (verify actual field names later)
- `JamendoApiResponse<T>` wrapper if response shape requires

6) Domain Layer (under `com.example.mymusic.domain`)
- `Track` entity: id, title, artist, durationMs, artworkUrl, audioUrl, isDownloadable (default false if unknown)
- `TrackRepository` interface: `suspend fun getTrendingTracks(limit: Int, offset: Int): List<Track>` (temporary; will migrate to Paging in next story)
- `GetTrendingTracksUseCase` (temporary signature accepting paging params)

7) Data Layer (under `com.example.mymusic.data.repository` and `mapper`)
- `TrackRepositoryImpl` calling `JamendoTracksService`, mapping DTO → domain
- Mapper functions created and unit-tested minimally for null/empty handling (simple tests OK)

8) App Scaffolding
- Material3 theme configured; `MainActivity` hosts `NavHost` with a placeholder `Home` destination
- Placeholder composable shows an empty state text "MyMusic ready"

9) Project builds and runs
- Successful sync and build in debug
- App installs and shows placeholder UI without runtime crashes

## Technical Notes
- Retrofit should attach `client_id` and common params via either:
  - explicit query params in service functions, or
  - an OkHttp interceptor that appends defaults when missing
- Json config: `ignoreUnknownKeys = true`, `coerceInputValues = true`
- Prefer `@Serializable` DTOs; use `retrofit2-kotlinx-serialization-converter`
- Use `BuildConfig.DEBUG` to enable OkHttp logging interceptor

## Test Cases
- Build passes; app launches and renders placeholder screen
- DI graph validates at runtime (no missing bindings)
- Calling `TrackRepository.getTrendingTracks(20,0)` in a local test returns mapped list when provided a fake service response

## Definition of Done
- All acceptance criteria met
- Code formatted, compiles, and lints cleanly
- Minimal unit tests for mappers and repository happy-path
- Documentation: brief README section added to `docs/` or code comments for DI graph overview

## Tasks (Suggested Breakdown)
- Update Gradle + add dependencies + Compose enablement
- Add BuildConfig field + client_id property
- Create `MyMusicApp` and update `AndroidManifest.xml`
- Implement DI modules (Network/Repository/UseCase/Image)
- Define DTOs + service + response wrapper
- Implement repository + mapper + simple tests
- Create Nav scaffolding + placeholder screen
- Build/run verification
