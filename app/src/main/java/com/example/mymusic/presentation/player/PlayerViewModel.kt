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

    fun toggle() = playbackController.togglePlayPause()
    fun seekTo(positionMs: Long) = playbackController.seekTo(positionMs)
}


