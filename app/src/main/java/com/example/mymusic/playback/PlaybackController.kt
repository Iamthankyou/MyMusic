package com.example.mymusic.playback

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import android.content.Intent
import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.mymusic.domain.model.Track
import javax.inject.Inject
import javax.inject.Singleton
import androidx.media3.common.AudioAttributes

@Singleton
class PlaybackController @Inject constructor(
    @ApplicationContext context: Context
) {
    private val appContext: Context = context.applicationContext
    private val player: ExoPlayer = ExoPlayer.Builder(appContext).build().apply {
        val attrs = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()
        setAudioAttributes(attrs, true)
    }

    private val _state = MutableStateFlow(NowPlayingState())
    val state: StateFlow<NowPlayingState> = _state

    private val _queue = MutableStateFlow<List<Track>>(emptyList())
    val queue: StateFlow<List<Track>> = _queue

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private val scope = CoroutineScope(Dispatchers.Main)

    init {
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

    fun play(
        trackId: String,
        title: String,
        artist: String,
        artworkUrl: String?,
        url: String
    ) {
        val item = MediaItem.Builder()
            .setMediaId(trackId)
            .setUri(url)
            .build()
        player.setMediaItem(item)
        player.prepare()
        player.play()
        // Ensure foreground service is running
        try {
            val intent = Intent(appContext, com.example.mymusic.playback.PlaybackService::class.java)
            appContext.startForegroundService(intent)
        } catch (_: Throwable) { }
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
                    MediaItem.Builder()
                        .setMediaId(track.id)
                        .setUri(track.audioUrl)
                        .build()
                }
                player.setMediaItems(mediaItems)
                player.prepare()
            }
        }
    }

    fun skipNext() {
        if (player.hasNextMediaItem()) {
            player.seekToNextMediaItem()
            scope.launch {
                val newIndex = _currentIndex.value + 1
                _currentIndex.emit(newIndex)
                updateStateFromCurrentTrack()
            }
        }
    }

    fun skipPrevious() {
        if (player.hasPreviousMediaItem()) {
            player.seekToPreviousMediaItem()
            scope.launch {
                val newIndex = _currentIndex.value - 1
                _currentIndex.emit(newIndex)
                updateStateFromCurrentTrack()
            }
        }
    }

    fun playAt(index: Int) {
        val tracks = _queue.value
        if (index in tracks.indices) {
            scope.launch {
                _currentIndex.emit(index)
                player.seekToDefaultPosition(index)
                updateStateFromCurrentTrack()
            }
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
                        artworkUrl = track.artworkUrl
                    )
                )
            }
        }
    }

    fun release() { player.release() }

    fun getPlayer(): ExoPlayer = player
}


