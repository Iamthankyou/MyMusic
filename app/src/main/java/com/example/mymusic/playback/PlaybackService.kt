package com.example.mymusic.playback

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.Player
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaybackService : MediaSessionService() {

    @Inject lateinit var controller: PlaybackController
    private var mediaSession: MediaSession? = null
    private var isForeground = false

    override fun onCreate() {
        super.onCreate()
        val session = MediaSession.Builder(this, controller.getPlayer()).build()
        mediaSession = session
        controller.getPlayer().addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                updateNotification()
            }
        })
        startForegroundWithNotification()
    }

    override fun onDestroy() {
        mediaSession?.release()
        mediaSession = null
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    private fun startForegroundWithNotification() {
        val channelId = "playback_channel"
        val nm = getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Playback", NotificationManager.IMPORTANCE_LOW)
            nm.createNotificationChannel(channel)
        }
        val notif = buildNotification(channelId)
        startForeground(NOTIF_ID, notif)
        isForeground = true
    }

    private fun updateNotification() {
        val channelId = "playback_channel"
        val notif = buildNotification(channelId)
        if (isForeground) {
            NotificationManagerCompat.from(this).notify(NOTIF_ID, notif)
        } else {
            startForeground(NOTIF_ID, notif)
            isForeground = true
        }
    }

    private fun buildNotification(channelId: String): Notification {
        val isPlaying = controller.getPlayer().isPlaying
        val toggleAction = NotificationCompat.Action(
            if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play,
            if (isPlaying) "Pause" else "Play",
            pendingIntent(ACTION_TOGGLE)
        )
        val stopAction = NotificationCompat.Action(
            android.R.drawable.ic_menu_close_clear_cancel,
            "Stop",
            pendingIntent(ACTION_STOP)
        )
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("MyMusic")
            .setContentText(if (isPlaying) "Playing" else "Paused")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setOngoing(isPlaying)
            .addAction(toggleAction)
            .addAction(stopAction)
            .build()
    }

    private fun pendingIntent(action: String): PendingIntent {
        val intent = Intent(this, PlaybackService::class.java).setAction(action)
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        return PendingIntent.getService(this, action.hashCode(), intent, flags)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_TOGGLE -> controller.togglePlayPause()
            ACTION_STOP -> {
                controller.pause()
                stopForeground(STOP_FOREGROUND_REMOVE)
                isForeground = false
            }
        }
        return START_STICKY
    }

    companion object {
        private const val NOTIF_ID = 1001
        private const val ACTION_TOGGLE = "com.example.mymusic.playback.ACTION_TOGGLE"
        private const val ACTION_STOP = "com.example.mymusic.playback.ACTION_STOP"
    }
}


