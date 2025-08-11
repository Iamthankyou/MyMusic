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

    fun release() { player.release() }

    fun getPlayer(): ExoPlayer = player
}


