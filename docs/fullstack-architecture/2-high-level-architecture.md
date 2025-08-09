# 2) High-Level Architecture
Layers:
- Presentation (Compose UI + ViewModels)
- Domain (Use Cases + Entities + Repository interfaces)
- Data (Repository implementations + Remote + Local)
- Media (Playback service, session, notifications)
- Download (DownloadManager + MediaStore integration)

Recommended initial module: single `app` module with clear package boundaries. Optional future split:
- `:app` (presentation + navigation)
- `:domain` (use cases + entities + repo contracts)
- `:data` (retrofit services, mappers, repo impls)
- `:media` (player service + playback)
