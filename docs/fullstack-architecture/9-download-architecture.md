# 9) Download Architecture
- Use Android `DownloadManager` for track audio URLs; register `BroadcastReceiver` for completion
- Target storage: Scoped `MediaStore.Audio` (Android 10+) with metadata
- Track progress: Query `DownloadManager` for status; expose via `Flow`
- Cancel/resume: Use `DownloadManager` APIs
- Map downloaded file URIs back to domain model for offline playback

Key classes:
- `DownloadController` (start/cancel/query)
- `DownloadRepository` (domain bridge)
- `DownloadReceiver` (completion)
