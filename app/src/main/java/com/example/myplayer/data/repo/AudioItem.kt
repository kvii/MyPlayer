package com.example.myplayer.data.repo

import android.net.Uri
import androidx.media3.common.MediaItem

/** 音频项目 */
data class AudioItem(
    val name: String,
    val contentUri: Uri,
    val mediaItem: MediaItem
)