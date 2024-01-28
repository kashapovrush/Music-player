package com.kashapovrush.musicplayer

import android.content.ComponentName
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.kashapovrush.musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playButton.setOnClickListener {
            ContextCompat.startForegroundService(
                this,
                MusicService.newIntent(this).apply {
                    action = "play"
                }
            )
        }

        binding.pauseBtn.setOnClickListener {
            ContextCompat.startForegroundService(
                this,
                MusicService.newIntent(this).apply {
                    action = "pause"
                }
            )
        }
    }

//    val serviceConnection = object : ServiceConnection {
//        override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder?) {
//            val localBinder = (iBinder as? MusicService.LocalBinder) ?: return
//            val service = localBinder.getServiceBinder()
//
//        }
//
//        override fun onServiceDisconnected(p0: ComponentName?) {
//        }
//
//    }

    override fun onStart() {
        super.onStart()
//        bindService(
//            MusicService.newIntent(this),
//            serviceConnection,
//            0
//        )
    }

    override fun onStop() {
        super.onStop()
//        unbindService(serviceConnection)
    }
}