# MyMusic — Android Music App (Jamendo)

Modern Android app built with 100% Kotlin, Jetpack Compose UI, Hilt DI, MVVM + Clean Architecture, Paging 3, Media3. Data is powered by the Jamendo API.

## Tech Stack
- Kotlin, Coroutines/Flow
- Jetpack Compose (Material 3, Navigation)
- Hilt (DI)
- Retrofit + OkHttp + Kotlinx Serialization
- Paging 3 (infinite scroll)
- Media3 (player; future stories)
- Coil (images)

## Requirements
- Android Studio (latest)
- JDK 17 (toolchain auto-provision configured)
- Android SDK 34 (compile/target)
- minSdk 24

## Quick Start
1) Clone and open in Android Studio
2) Configure Jamendo Client ID
   - Set in `gradle.properties`:
     - `JAMENDO_CLIENT_ID=YOUR_CLIENT_ID`
   - Already set by default in this repo; replace with your own when needed
3) Build & run
   - Build: `./gradlew :app:assembleDebug`
   - Install: `./gradlew :app:installDebug`
   - Or run from Android Studio on an emulator/device

## Project Structure (high-level)
- `app/`
  - `presentation/` — Compose screens, ViewModels
  - `domain/` — models, repositories (interfaces), use cases
  - `data/` — remote services (Retrofit), repository impls, mappers, paging
  - `di/` — Hilt modules
  - `media/` (future) — Media3 player & service
- `docs/` — PRD, front-end spec, architecture, stories

## Configuration
- Network base URL: `https://api.jamendo.com/v3.0`
- Client ID is injected via `BuildConfig.JAMENDO_CLIENT_ID`
- Default query params (client_id, format=json) are added by OkHttp interceptor

## Runbook
- Trending Tracks screen (Story 1.2) loads a paged list with loading/error/empty states
- Player/downloads will be implemented in upcoming stories

## Contributing (How to)
1) Issues & Planning
   - Create/assign an issue or pick a story under `docs/stories/`
   - Align changes with PRD/Architecture documents in `docs/`

2) Branching
   - Branch from `main` using Conventional Branch Names:
     - `feat/<short-scope>` — new feature
     - `fix/<short-scope>` — bug fix
     - `chore/<short-scope>` — tooling/infra/docs
   - Keep branches small and focused

3) Commits
   - Follow Conventional Commits:
     - `feat: ...`, `fix: ...`, `chore: ...`, `refactor: ...`, `docs: ...`, `test: ...`
   - Write clear, actionable messages

4) Code Style & Architecture
   - Kotlin official style; MVVM + Clean Architecture separation
   - Compose best practices (stateless UI where possible, `collectAsStateWithLifecycle`, stable item keys)
   - DI via Hilt; avoid service locators/singletons outside DI
   - Keep DTOs out of `domain`/`presentation` (map in `data.mapper`)
   - Paging: use `Pager` + `PagingSource`; handle load states

5) Tests
   - Unit tests for mappers and use cases where applicable
   - Optional: UI tests for key flows (Trending → scroll; later Player)
   - Run: `./gradlew test`

6) Lint/Build
   - Build locally: `./gradlew :app:assembleDebug`
   - Ensure no warnings/errors introduced where feasible

7) Pull Requests
   - Rebase on latest `main` before opening PR
   - Include:
     - Scope/Impact summary
     - Screenshots/GIFs for UI changes
     - Checklist: build passes, basic tests pass, stories/PRD alignment (if applicable)
   - Request review; address feedback promptly

8) Secrets & Permissions
   - Do not commit real secrets; use `gradle.properties` for local config
   - INTERNET permission is required and already declared

## Troubleshooting
- INTERNET SecurityException → ensure `<uses-permission android:name="android.permission.INTERNET" />` is present (already configured)
- Empty Trending list → verify `JAMENDO_CLIENT_ID` is valid and device has network
- Kapt/JDK errors → toolchain is configured; ensure Android Studio uses Gradle JDK 17

## Roadmap (short)
- Player: Media3 service + notification controls
- Explore paging + more content types (albums/playlists/podcasts)
- Radio/Stream categories
- Downloads via DownloadManager + MediaStore

## License
- Review Jamendo API terms and any attribution requirements before distribution.
