package com.example.myplayer.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myplayer.R
import com.example.myplayer.data.repo.AudioItem

@Composable
fun PlayList(
    list: List<AudioItem>,
    onRemove: (Int) -> Unit = {},
) {
    val showState = remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (showState.value) {
            TextButton(onClick = { showState.value = false }) {
                Text(stringResource(R.string.collapse))
            }
        } else {
            TextButton(onClick = { showState.value = true }) {
                Text(stringResource(R.string.expand))
            }
        }

        if (showState.value) {
            LazyColumn(
                modifier = Modifier.height(100.dp)
            ) {
                items(list.size) { index ->
                    AudioItemTile(
                        item = list[index],
                        onRemove = { onRemove(index) },
                    )
                }
            }
        }
    }
}

@Composable
fun AudioItemTile(
    item: AudioItem,
    onRemove: () -> Unit,
) {
    Row(
        modifier = Modifier.height(48.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(item.name, style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.weight(1f))

        TextButton(
            onClick = onRemove,
        ) {
            Text(
                text = stringResource(R.string.remove),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.error
                )
            )
        }
    }
}
