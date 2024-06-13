package com.tiyi.tiyi_app.utils

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import java.io.File

class AudioPlayerService : Service() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        when (action) {
            ACTION_PLAY -> {
                val audioFilePath = intent?.getStringExtra(EXTRA_AUDIO_FILE_PATH)
                val audioFile = File(audioFilePath ?: "")
                Log.d("AudioPlayerService", "Playing audio file: ${audioFile.absolutePath}")
                play(audioFile)
            }

            ACTION_PAUSE -> {
                Log.d("AudioPlayerService", "Pausing audio")
                pause()
            }

            ACTION_RESUME -> {
                Log.d("AudioPlayerService", "Resuming audio")
                resume()
            }

            ACTION_STOP -> {
                Log.d("AudioPlayerService", "Stopping audio")
                stop()
            }
        }
        return START_NOT_STICKY
    }

    private fun play(audioFile: File) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(audioFile.absolutePath)
            setOnCompletionListener {
                Log.d("AudioPlayerService", "Audio completed")
                stopSelf()
            }
            setOnPreparedListener {
                Log.d("AudioPlayerService", "Audio prepared, starting playback")
                start()
            }
            setOnErrorListener { _, what, extra ->
                Log.e("AudioPlayerService", "Error occurred: what=$what, extra=$extra")
                false
            }
            prepareAsync() // Use prepareAsync for better responsiveness
        }
    }

    private fun pause() {
        mediaPlayer?.pause()
    }

    private fun resume() {
        mediaPlayer?.start()
    }

    private fun stop() {
        mediaPlayer?.release()
        mediaPlayer = null
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    companion object {
        const val ACTION_PLAY = "com.example.action.PLAY"
        const val ACTION_PAUSE = "com.example.action.PAUSE"
        const val ACTION_RESUME = "com.example.action.RESUME"
        const val ACTION_STOP = "com.example.action.STOP"
        const val EXTRA_AUDIO_FILE_PATH = "com.example.extra.AUDIO_FILE_PATH"
    }
}
