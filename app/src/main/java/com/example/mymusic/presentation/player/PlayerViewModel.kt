package com.example.mymusic.presentation.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.playback.NowPlayingState
import com.example.mymusic.playback.PlaybackController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    val playbackController: PlaybackController
) : ViewModel() {
    val nowPlaying: StateFlow<NowPlayingState> = playbackController.state
        .stateIn(viewModelScope, SharingStarted.Eagerly, NowPlayingState())

    // Playback controls
    fun toggle() = playbackController.togglePlayPause()
    fun seekTo(positionMs: Long) = playbackController.seekTo(positionMs)
    fun skipNext() = playbackController.skipNext()
    fun skipPrevious() = playbackController.skipPrevious()

    // Playback speed, repeat, shuffle controls
    val playbackSpeed = playbackController.playbackSpeed
        .stateIn(viewModelScope, SharingStarted.Eagerly, 1.0f)
    
    val repeatMode = playbackController.repeatMode
        .stateIn(viewModelScope, SharingStarted.Eagerly, androidx.media3.common.Player.REPEAT_MODE_OFF)
    
    val shuffleMode = playbackController.shuffleMode
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun setPlaybackSpeed(speed: Float) = playbackController.setPlaybackSpeed(speed)
    fun toggleRepeatMode() = playbackController.toggleRepeatMode()
    fun toggleShuffleMode() = playbackController.toggleShuffleMode()
}


