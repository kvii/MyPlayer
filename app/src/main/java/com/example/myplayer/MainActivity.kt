package com.example.myplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.ui.PlayerView
import com.example.myplayer.data.repo.AudioItem
import com.example.myplayer.data.repo.StopPlayerWorker
import com.example.myplayer.ui.component.AudioPicker
import com.example.myplayer.ui.component.MinuteField
import com.example.myplayer.ui.component.PlayList
import com.example.myplayer.ui.theme.MyPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPlayerTheme {
                MyPage()
            }
        }
    }
}

@Composable
fun MyPage() {
    val context = LocalContext.current
    val player = (context.applicationContext as MyApplication).player

    val isRunning = remember { mutableStateOf(false) }
    val playList = remember { mutableStateListOf<AudioItem>() }
    val minute = remember { mutableStateOf<Int?>(null) }
    val showPicker = remember { mutableStateOf(false) }
    val audioId = remember { mutableIntStateOf(1) }

    if (showPicker.value) {
        AudioPicker(
            onSelect = { uri ->
                uri?.let {
                    val mediaItem = MediaItem.fromUri(it)
                    playList.add(
                        AudioItem(
                            name = "曲目 ${audioId.intValue}",
                            contentUri = it,
                            mediaItem = mediaItem,
                        )
                    )
                    player.addMediaItem(mediaItem)
                    audioId.intValue++
                }
                showPicker.value = false
            },
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.safeContent.asPaddingValues()),
        topBar = { Text(stringResource(R.string.app_name)) },
        floatingActionButton = {
            if (!isRunning.value) {
                FloatingActionButton(
                    onClick = { showPicker.value = true }
                ) {
                    Icon(
                        Icons.Sharp.Add,
                        contentDescription = stringResource(R.string.add_audio),
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            // 第一步 选曲
            Text(stringResource(R.string.intro_1))
            PlayList(
                list = playList,
                onRemove = { index ->
                    playList.removeAt(index)
                    player.removeMediaItem(index)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 第二步 定时
            Text(stringResource(R.string.intro_2))
            MinuteField(
                minute = minute.value,
                readOnly = isRunning.value,
                enabled = playList.isNotEmpty(),
                onChange = { minute.value = it },
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 第三步 启动
            Text(stringResource(R.string.intro_3))
            TextButton(
                onClick = {
                    if (isRunning.value) {
                        // 停止
                        isRunning.value = false
                        player.stop()
                        StopPlayerWorker.cancel(context)
                    } else {
                        // 启动
                        isRunning.value = true
                        player.prepare()
                        player.play()
                        StopPlayerWorker.schedule(context, minute.value!!)
                    }
                },
                enabled = minute.value.let { it != null && it > 0 },
            ) {
                if (isRunning.value) {
                    Text(stringResource(R.string.terminate))
                } else {
                    Text(stringResource(R.string.start))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 播放器 生命周期
            val lifecycle = remember {
                mutableStateOf(Lifecycle.Event.ON_CREATE)
            }
            val lifecycleOwner = LocalLifecycleOwner.current
            DisposableEffect(lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    lifecycle.value = event
                }

                lifecycleOwner.lifecycle.addObserver(observer)

                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }

            // 播放器 UI
            AndroidView(
                factory = { context ->
                    PlayerView(context).also {
                        it.player = player
                    }
                },
                update = {
                    when (lifecycle.value) {
                        Lifecycle.Event.ON_PAUSE -> {
                            it.onPause()
                        }

                        Lifecycle.Event.ON_RESUME -> {
                            it.onResume()
                        }

                        else -> {}
                    }
                }
            )
        }
    }
}
