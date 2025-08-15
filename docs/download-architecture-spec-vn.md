# Đặc Tả Kiến Trúc Tính Năng Download - MyMusic App

## Tổng Quan Kiến Trúc

Tính năng download trong MyMusic được thiết kế theo **MVVM Clean Architecture** với 3 layer chính:

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                      │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────┐ │
│  │ DownloadsScreen │  │ DownloadButton  │  │ TrackItem   │ │
│  │ DownloadsViewModel│ │ DownloadProgress│ │ (enhanced)  │ │
│  └─────────────────┘  │ Indicator       │  └─────────────┘ │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     DOMAIN LAYER                          │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────┐ │
│  │DownloadRepository│  │DownloadTrackUse │  │Track Model │ │
│  │   Interface     │  │Case             │  │             │ │
│  └─────────────────┘  └─────────────────┘  └─────────────┘ │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      DATA LAYER                           │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────┐ │
│  │DownloadRepository│  │DownloadService  │  │DownloadDao  │ │
│  │   Impl          │  │DownloadProgress │  │AppDatabase  │ │
│  └─────────────────┘  │Monitor          │  └─────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

## 🏗️ Chi Tiết Kiến Trúc

### 1. PRESENTATION LAYER

#### 1.1 DownloadsScreen.kt
**Vai trò:** UI chính để quản lý downloads
**Kiến trúc:** Compose UI với ViewModel pattern

```kotlin
@Composable
fun DownloadsScreen(
    viewModel: DownloadsViewModel = hiltViewModel(),
    onNavigateToPlayer: (String) -> Unit = {}
)
```

**Các Component chính:**
- `ActiveDownloadItem`: Hiển thị download đang tiến hành
- `CompletedDownloadItem`: Hiển thị download đã hoàn thành
- Search bar để lọc downloads
- Empty state khi không có download

**State Management:**
```kotlin
data class DownloadsState(
    val activeDownloads: List<DownloadEntity>,
    val completedDownloads: List<DownloadEntity>,
    val searchQuery: String
)
```

#### 1.2 DownloadsViewModel.kt
**Vai trò:** Quản lý business logic và state cho DownloadsScreen
**Dependencies:** Use cases và PlaybackController

**Key Methods:**
```kotlin
class DownloadsViewModel @Inject constructor(
    private val getDownloadedTracksUseCase: GetDownloadedTracksUseCase,
    private val deleteDownloadUseCase: DeleteDownloadUseCase,
    private val cancelDownloadUseCase: CancelDownloadUseCase,
    private val playbackController: PlaybackController
) : ViewModel()
```

**State Flows:**
- `_downloadsState`: State chính của screen
- `_searchQuery`: Query tìm kiếm
- `activeDownloads`: Downloads đang tiến hành
- `completedDownloads`: Downloads đã hoàn thành

#### 1.3 DownloadButton Component
**Vai trò:** Component tái sử dụng cho download button
**States:** Download, Downloading, Downloaded, Failed, Pending

```kotlin
@Composable
fun DownloadButton(
    downloadStatus: DownloadStatus?,
    progress: Int = 0,
    onDownloadClick: () -> Unit,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier
)
```

### 2. DOMAIN LAYER

#### 2.1 DownloadRepository Interface
**Vai trò:** Contract cho download operations
**Methods chính:**
```kotlin
interface DownloadRepository {
    suspend fun downloadTrack(track: Track, priority: DownloadPriority): Result<Long>
    suspend fun cancelDownload(trackId: String): Result<Unit>
    suspend fun deleteDownload(trackId: String): Result<Unit>
    fun getAllDownloads(): Flow<List<DownloadEntity>>
    suspend fun updateDownloadId(trackId: String, downloadId: Long): Result<Unit>
    suspend fun updateDownloadStatus(trackId: String, status: DownloadStatus, progress: Int): Result<Unit>
}
```

#### 2.2 Use Cases
**DownloadTrackUseCase:**
```kotlin
class DownloadTrackUseCase @Inject constructor(
    private val downloadRepository: DownloadRepository
) {
    suspend operator fun invoke(
        track: Track,
        priority: DownloadPriority = DownloadPriority.NORMAL
    ): Result<Long>
}
```

**GetDownloadedTracksUseCase:**
```kotlin
class GetDownloadedTracksUseCase @Inject constructor(
    private val downloadRepository: DownloadRepository
) {
    operator fun invoke(): Flow<List<DownloadEntity>>
}
```

**DeleteDownloadUseCase:**
```kotlin
class DeleteDownloadUseCase @Inject constructor(
    private val downloadRepository: DownloadRepository
) {
    suspend operator fun invoke(trackId: String): Result<Unit>
}
```

### 3. DATA LAYER

#### 3.1 DownloadRepositoryImpl
**Vai trò:** Implementation của DownloadRepository interface
**Dependencies:** Context, DownloadDao

**Key Implementation:**
```kotlin
class DownloadRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val downloadDao: DownloadDao
) : DownloadRepository
```

**Download Flow:**
1. Kiểm tra track đã download chưa
2. Tạo DownloadEntity và lưu vào database
3. Start DownloadService với track info
4. Update downloadId và status

#### 3.2 DownloadService
**Vai trò:** IntentService để handle download operations
**Architecture:** Android IntentService pattern

```kotlin
@AndroidEntryPoint
class DownloadService : IntentService("DownloadService")
```

**Download Process:**
1. Nhận track info từ Intent extras
2. Tạo DownloadManager.Request
3. Enqueue download với DownloadManager
4. Update database với downloadId và status

#### 3.3 DownloadProgressMonitor
**Vai trò:** Monitor download progress real-time
**Architecture:** Background coroutine với polling

```kotlin
class DownloadProgressMonitor(
    private val context: Context,
    private val downloadRepository: DownloadRepository
)
```

**Monitoring Process:**
- Poll DownloadManager mỗi giây
- Update progress và status trong database
- Handle download completion/failure

#### 3.4 Database Layer
**DownloadEntity:**
```kotlin
@Entity(tableName = "downloads")
data class DownloadEntity(
    @PrimaryKey val trackId: String,
    val title: String,
    val artist: String,
    val album: String?,
    val duration: Long,
    val imageUrl: String?,
    val audioUrl: String,
    val localFilePath: String,
    val fileSize: Long,
    val downloadDate: LocalDateTime = LocalDateTime.now(),
    val status: DownloadStatus = DownloadStatus.PENDING,
    val progress: Int = 0,
    val downloadId: Long? = null,
    val priority: DownloadPriority = DownloadPriority.NORMAL
)
```

**DownloadDao:**
```kotlin
@Dao
interface DownloadDao {
    @Query("SELECT * FROM downloads WHERE status != 'DELETED' ORDER BY priority DESC, downloadDate ASC")
    fun getAllDownloads(): Flow<List<DownloadEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDownload(download: DownloadEntity)
    
    @Query("UPDATE downloads SET status = :status, progress = :progress WHERE trackId = :trackId")
    fun updateDownloadStatus(trackId: String, status: DownloadStatus, progress: Int)
}
```

## 🔄 Data Flow

### Download Flow:
```
User clicks download → ViewModel → UseCase → Repository → Service → DownloadManager
                                                                    ↓
Database ← ProgressMonitor ← DownloadManager ← File System
```

### Progress Update Flow:
```
DownloadManager → ProgressMonitor → Repository → DAO → Database → ViewModel → UI
```

## 🧩 Dependency Injection

### Hilt Modules:

#### RepositoryModule:
```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindDownloadRepository(impl: DownloadRepositoryImpl): DownloadRepository
    
    companion object {
        @Provides
        @Singleton
        fun provideDownloadDao(database: AppDatabase): DownloadDao = database.downloadDao()
        
        @Provides
        @Singleton
        fun provideDownloadProgressMonitor(
            @ApplicationContext context: Context,
            downloadRepository: DownloadRepository
        ): DownloadProgressMonitor = DownloadProgressMonitor(context, downloadRepository)
    }
}
```

#### UseCaseModule:
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideDownloadTrackUseCase(
        downloadRepository: DownloadRepository
    ): DownloadTrackUseCase = DownloadTrackUseCase(downloadRepository)
    
    @Provides
    @Singleton
    fun provideGetDownloadedTracksUseCase(
        downloadRepository: DownloadRepository
    ): GetDownloadedTracksUseCase = GetDownloadedTracksUseCase(downloadRepository)
}
```

## 🔧 Technical Implementation Details

### 1. File Storage Strategy
**Location:** `Environment.DIRECTORY_MUSIC` trong app's external files directory
**Naming:** `{trackId}.mp3`
**Path:** `/storage/emulated/0/Android/data/com.example.mymusic/files/Music/`

### 2. Download Priority System
```kotlin
enum class DownloadPriority {
    HIGH,    // 1000
    NORMAL,  // 0
    LOW      // -1000
}
```

### 3. Progress Calculation
```kotlin
val progress = ((bytesDownloaded * 100) / totalSize).toInt()
```

### 4. Error Handling
- Network failures → Update status to FAILED
- File system errors → Log và update status
- Database errors → Retry với exponential backoff

## 📱 UI Integration Points

### 1. TrackItem Enhancement
```kotlin
@Composable
fun TrackItem(
    track: Track,
    onClick: () -> Unit,
    downloadStatus: DownloadStatus? = null,
    onDownloadClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
)
```

### 2. TrackDetailScreen Integration
```kotlin
@Composable
private fun TrackDetailHeader(
    track: Track,
    onPlayClick: () -> Unit,
    onDownloadClick: () -> Unit
)
```

### 3. TrendingScreen Integration
```kotlin
@Composable
private fun TrackRow(
    track: Track,
    onClick: () -> Unit = {},
    onDownloadClick: () -> Unit = {}
)
```

## 🚀 Performance Considerations

### 1. Database Operations
- Sử dụng `withContext(Dispatchers.IO)` cho tất cả database operations
- Flow-based reactive updates
- Efficient queries với proper indexing

### 2. Download Management
- Polling interval: 1 second (có thể tối ưu)
- Background processing với IntentService
- Memory-efficient progress tracking

### 3. UI Updates
- StateFlow cho reactive updates
- Efficient recomposition với Compose
- Lazy loading cho large download lists

## 🔒 Security & Permissions

### Required Permissions:
```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="28" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" android:maxSdkVersion="32" />
```

### File Access:
- Files được lưu trong app's private directory
- Không expose ra external storage
- Proper file permissions

## 🧪 Testing Strategy

### 1. Unit Tests
- Repository methods với mocked dependencies
- Use case business logic
- ViewModel state management

### 2. Integration Tests
- Download flow end-to-end
- Database operations
- Service integration

### 3. UI Tests
- Compose component behavior
- User interaction flows
- State updates

## 📊 Monitoring & Analytics

### 1. Download Metrics
- Success/failure rates
- Download speeds
- Storage usage

### 2. Performance Metrics
- Database operation times
- UI update frequencies
- Memory usage patterns

## 🔮 Future Enhancements

### 1. Architecture Improvements
- Migrate to WorkManager cho background tasks
- Implement proper retry mechanisms
- Add download queuing system

### 2. Feature Additions
- Batch download operations
- Download scheduling
- Cloud sync integration

---

**Ngày cập nhật:** 16/08/2025  
**Phiên bản:** 1.0  
**Trạng thái:** Hoàn thành Phase 1  
**Kiến trúc:** MVVM Clean Architecture với Hilt DI
