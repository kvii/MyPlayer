package com.example.myplayer

import android.app.Application
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

class MyApplication : Application() {
    lateinit var player: Player
        private set

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()
    }

    override fun onTerminate() {
        super.onTerminate()
        player.release()
    }
}