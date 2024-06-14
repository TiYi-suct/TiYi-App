package com.tiyi.tiyi_app.utils

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log

class AudioPlayerService : Service() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("AudioPlayerService", "onStartCommand called with action: ${intent?.action}")
        when (intent?.action) {
            ACTION_PLAY -> {
                val url = intent.getStringExtra(EXTRA_AUDIO_URL)
                Log.d("AudioPlayerService", "ACTION_PLAY received with URL: $url")
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer().apply {
                        setDataSource(url)
                        setOnPreparedListener {
                            Log.d("AudioPlayerService", "MediaPlayer prepared, starting playback")
                            start()
                        }
                        prepareAsync()
                    }
                } else {
                    mediaPlayer?.start()
                    Log.d("AudioPlayerService", "MediaPlayer resumed playback")
                }
            }

            ACTION_PAUSE -> {
                Log.d("AudioPlayerService", "ACTION_PAUSE received")
                mediaPlayer?.pause()
            }

            ACTION_RESUME -> {
                Log.d("AudioPlayerService", "ACTION_RESUME received")
                mediaPlayer?.start()
            }

            ACTION_STOP -> {
                Log.d("AudioPlayerService", "ACTION_STOP received")
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
            }

            else -> {
                Log.d("AudioPlayerService", "Unknown action received")
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val ACTION_PLAY = "com.example.ACTION_PLAY"
        const val ACTION_PAUSE = "com.example.ACTION_PAUSE"
        const val ACTION_RESUME = "com.example.ACTION_RESUME"
        const val ACTION_STOP = "com.example.ACTION_STOP"
        const val EXTRA_AUDIO_URL = "com.example.EXTRA_AUDIO_URL"
    }
}

