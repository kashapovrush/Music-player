package com.kashapovrush.musicplayer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MusicService : Service() {

    private lateinit var player: MediaPlayer
    private var length = 0

    inner class LocalBinder : Binder() {

        fun getServiceBinder() = this@MusicService

    }

    var onNameSongChanged: ((String) -> Unit)? = null

    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer.create(this, R.raw.measurements)
        createNotificationChanel()
        startForeground(NOTIFICATION_ID, createNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        CoroutineScope(Dispatchers.Main).launch {
            val action = intent?.action
            if (action == "play") {
                if (player.isPlaying) {

                } else {
                    player.seekTo(length)
                    player.start()
                    onNameSongChanged?.invoke("Нуки - Измерения")
                }
            } else if (action == "pause") {
                player.pause()
                length = player.currentPosition
            }
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {

        return LocalBinder()
    }

    private fun createNotificationChanel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }


    private fun createNotification(): Notification {
        val playIntent = Intent(this, MusicService::class.java).apply {
           action = "play"
        }
        val playPendingIntent = PendingIntent.getService(
            this, 1, playIntent, PendingIntent.FLAG_IMMUTABLE
        )
        val pauseIntent = Intent(this, MusicService::class.java).apply {
            action = "pause"
        }
        val pausePendingIntent = PendingIntent.getService(
            this, 1, pauseIntent, PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_icon_music)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(
                NotificationCompat.Action(R.drawable.ic_play, "Play", playPendingIntent)
            )
            .addAction(
                NotificationCompat.Action(R.drawable.ic_pause, "Pause", pausePendingIntent)
            )
            .build()
    }

    companion object {

        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
        private const val NOTIFICATION_ID = 101

        fun newIntent(context: Context): Intent {
            return Intent(context, MusicService::class.java)
        }
    }
}