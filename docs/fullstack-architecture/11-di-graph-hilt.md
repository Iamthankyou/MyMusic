# 11) DI Graph (Hilt)
Modules:
- `NetworkModule`: OkHttp, Retrofit, Json, Services
- `RepositoryModule`: Binds repo interfaces to impls
- `UseCaseModule`: Provides use cases
- `MediaModule`: ExoPlayer, MediaSession, PlaybackController
- `DownloadModule`: DownloadManager, DownloadController
- `ImageModule`: Coil ImageLoader

Scopes:
- `@Singleton` for Retrofit, Repositories, ExoPlayer, Controllers
- `@ViewModelScoped` for use cases if preferred, otherwise singleton providers
