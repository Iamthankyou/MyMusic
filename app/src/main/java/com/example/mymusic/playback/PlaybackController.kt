package com.example.mymusic.playback

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import android.content.Intent
import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaMetadata
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.mymusic.domain.model.Track
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaybackController @Inject constructor(
    @ApplicationContext context: Context
) {
    private val appContext: Context = context.applicationContext
    private val audioManager: AudioManager = appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    
    private val player: ExoPlayer = ExoPlayer.Builder(appContext).build().apply {
        val attrs = androidx.media3.common.AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()
        setAudioAttributes(attrs, true)
    }
    
    private var audioFocusRequest: AudioFocusRequest? = null
    private var audioFocusListener: AudioManager.OnAudioFocusChangeListener? = null

    private val _state = MutableStateFlow(NowPlayingState())
    val state: StateFlow<NowPlayingState> = _state

    private val _queue = MutableStateFlow<List<Track>>(emptyList())
    val queue: StateFlow<List<Track>> = _queue

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed

    private val _repeatMode = MutableStateFlow(Player.REPEAT_MODE_OFF)
    val repeatMode: StateFlow<Int> = _repeatMode

    private val _shuffleMode = MutableStateFlow(false)
    val shuffleMode: StateFlow<Boolean> = _shuffleMode

    private val scope = CoroutineScope(Dispatchers.Main)

    init {
        setupAudioFocus()
        
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                scope.launch { _state.emit(_state.value.copy(isPlaying = isPlaying)) }
            }
        })
        
        // Periodic position updater
        scope.launch {
            while (true) {
                val duration = player.duration.takeIf { it != C.TIME_UNSET } ?: 0L
                val position = player.currentPosition
                _state.emit(_state.value.copy(positionMs = position, durationMs = duration))
                kotlinx.coroutines.delay(500L)
            }
        }
    }
    
    private fun setupAudioFocus() {
        audioFocusListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN -> {
                    // Resume playback and restore volume
                    player.volume = 1.0f
                    if (!player.isPlaying) player.play()
                }
                AudioManager.AUDIOFOCUS_LOSS -> {
                    // Pause playback
                    player.pause()
                }
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                    // Pause playback temporarily
                    player.pause()
                }
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                    // Lower volume temporarily
                    player.volume = 0.3f
                }
            }
        }
    }

    fun play(
        trackId: String,
        title: String,
        artist: String,
        artworkUrl: String?,
        url: String
    ) {
        // Request audio focus - but don't block if it fails
        requestAudioFocus()
        
        val metadata = MediaMetadata.Builder()
            .setTitle(title)
            .setArtist(artist)
            .setArtworkUri(artworkUrl?.let { android.net.Uri.parse(it) })
            .build()
            
        val item = MediaItem.Builder()
            .setMediaId(trackId)
            .setUri(url)
            .setMediaMetadata(metadata)
            .build()
        
        // Set the media item and prepare
        player.setMediaItem(item)
        player.prepare()
        
        // Start playing immediately
        player.play()
        
        // Ensure foreground service is running
        try {
            val intent = Intent(appContext, com.example.mymusic.playback.PlaybackService::class.java)
            appContext.startForegroundService(intent)
        } catch (_: Throwable) { }
        
        // Update state immediately
        scope.launch {
            _state.emit(
                NowPlayingState(
                    trackId = trackId,
                    title = title,
                    artist = artist,
                    artworkUrl = artworkUrl,
                    isPlaying = true,
                    positionMs = 0L,
                    durationMs = 0L
                )
            )
        }
    }
    
    private fun requestAudioFocus(): Boolean {
        val audioFocusListener = audioFocusListener ?: return false
        
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
                
            audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(audioAttributes)
                .setOnAudioFocusChangeListener(audioFocusListener)
                .setAcceptsDelayedFocusGain(true)
                .build()
                
            audioManager.requestAudioFocus(audioFocusRequest!!)
        } else {
            @Suppress("DEPRECATION")
            audioManager.requestAudioFocus(
                audioFocusListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )
        } == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
    }

    fun togglePlayPause() {
        if (player.isPlaying) player.pause() else player.play()
    }

    fun pause() { player.pause() }

    fun seekTo(positionMs: Long) {
        val duration = player.duration.takeIf { it != C.TIME_UNSET } ?: return
        val clamped = positionMs.coerceIn(0L, duration)
        player.seekTo(clamped)
        scope.launch { _state.emit(_state.value.copy(positionMs = clamped, durationMs = duration)) }
    }

    fun setQueue(tracks: List<Track>) {
        scope.launch {
            _queue.emit(tracks)
            if (tracks.isNotEmpty()) {
                val mediaItems = tracks.map { track ->
                    val metadata = MediaMetadata.Builder()
                        .setTitle(track.title)
                        .setArtist(track.artist)
                        .setArtworkUri(track.artworkUrl?.let { android.net.Uri.parse(it) })
                        .build()
                        
                    MediaItem.Builder()
                        .setMediaId(track.id)
                        .setUri(track.audioUrl)
                        .setMediaMetadata(metadata)
                        .build()
                }
                
                // Only set media items if player is not currently playing
                if (!player.isPlaying) {
                    player.setMediaItems(mediaItems)
                    player.prepare()
                }
            }
        }
    }

    fun skipNext() {
        val tracks = _queue.value
        val currentIndex = _currentIndex.value
        if (currentIndex < tracks.size - 1) {
            val newIndex = currentIndex + 1
            scope.launch {
                _currentIndex.emit(newIndex)
                player.seekToDefaultPosition(newIndex)
                player.play()
                updateStateFromCurrentTrack()
            }
        }
    }

    fun skipPrevious() {
        val tracks = _queue.value
        val currentIndex = _currentIndex.value
        if (currentIndex > 0) {
            val newIndex = currentIndex - 1
            scope.launch {
                _currentIndex.emit(newIndex)
                player.seekToDefaultPosition(newIndex)
                player.play()
                updateStateFromCurrentTrack()
            }
        }
    }

    fun playAt(index: Int) {
        val tracks = _queue.value
        if (index in tracks.indices) {
            scope.launch {
                _currentIndex.emit(index)
                
                // Set media items if not already set
                if (player.mediaItemCount == 0) {
                    val mediaItems = tracks.map { track ->
                        val metadata = MediaMetadata.Builder()
                            .setTitle(track.title)
                            .setArtist(track.artist)
                            .setArtworkUri(track.artworkUrl?.let { android.net.Uri.parse(it) })
                            .build()
                            
                        MediaItem.Builder()
                            .setMediaId(track.id)
                            .setUri(track.audioUrl)
                            .setMediaMetadata(metadata)
                            .build()
                    }
                    player.setMediaItems(mediaItems)
                    player.prepare()
                }
                
                player.seekToDefaultPosition(index)
                // Request audio focus before playing
                requestAudioFocus()
                player.play() // Actually start playing
                updateStateFromCurrentTrack()
                
                // Ensure foreground service is running
                try {
                    val intent = Intent(appContext, com.example.mymusic.playback.PlaybackService::class.java)
                    appContext.startForegroundService(intent)
                } catch (_: Throwable) { }
            }
        }
    }
    
    fun setCurrentIndex(index: Int) {
        scope.launch {
            _currentIndex.emit(index)
        }
    }
    
    fun updateQueueOnly(tracks: List<Track>, currentIndex: Int) {
        scope.launch {
            _queue.emit(tracks)
            _currentIndex.emit(currentIndex)
        }
    }

    private fun updateStateFromCurrentTrack() {
        val tracks = _queue.value
        val currentIndex = _currentIndex.value
        if (currentIndex in tracks.indices) {
            val track = tracks[currentIndex]
            scope.launch {
                _state.emit(
                    _state.value.copy(
                        trackId = track.id,
                        title = track.title,
                        artist = track.artist,
                        artworkUrl = track.artworkUrl,
                        isPlaying = player.isPlaying
                    )
                )
            }
        }
    }

    fun release() { 
        abandonAudioFocus()
        player.release() 
    }
    
    private fun abandonAudioFocus() {
        audioFocusListener?.let { listener ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                audioFocusRequest?.let { request ->
                    audioManager.abandonAudioFocusRequest(request)
                }
            } else {
                @Suppress("DEPRECATION")
                audioManager.abandonAudioFocus(listener)
            }
        }
        audioFocusRequest = null
    }

    fun getPlayer(): ExoPlayer = player

    // Playback Speed Control
    fun setPlaybackSpeed(speed: Float) {
        val clampedSpeed = speed.coerceIn(0.25f, 2.0f)
        player.playbackParameters = androidx.media3.common.PlaybackParameters(clampedSpeed)
        scope.launch {
            _playbackSpeed.emit(clampedSpeed)
        }
    }

    // Repeat Mode Control
    fun setRepeatMode(mode: Int) {
        player.repeatMode = mode
        scope.launch {
            _repeatMode.emit(mode)
        }
    }

    fun toggleRepeatMode() {
        val currentMode = _repeatMode.value
        val nextMode = when (currentMode) {
            Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ONE
            Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
            Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_OFF
            else -> Player.REPEAT_MODE_OFF
        }
        setRepeatMode(nextMode)
    }

    // Shuffle Mode Control
    fun toggleShuffleMode() {
        val newShuffleMode = !_shuffleMode.value
        player.shuffleModeEnabled = newShuffleMode
        scope.launch {
            _shuffleMode.emit(newShuffleMode)
        }
    }

    fun setShuffleMode(enabled: Boolean) {
        player.shuffleModeEnabled = enabled
        scope.launch {
            _shuffleMode.emit(enabled)
        }
    }
}


