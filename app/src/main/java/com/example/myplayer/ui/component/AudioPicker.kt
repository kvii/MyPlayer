package com.example.myplayer.ui.component

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.myplayer.R


@Composable
fun AudioPicker(
    onSelect: (Uri?) -> Unit = {},
) {
    val valueState = remember { mutableStateOf("") }

    val selectAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.toString()?.also { valueState.value = it }
        }
    )

    Dialog(
        onDismissRequest = { onSelect(null) }
    ) {
        Card(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                TextField(
                    value = valueState.value,
                    onValueChange = { valueState.value = it },
                    label = { Text(stringResource(R.string.pick_uri)) },
                    placeholder = { Text(stringResource(R.string.uri_input_hint)) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    TextButton(
                        onClick = { selectAudioLauncher.launch("audio/mpeg") },
                    ) {
                        Text(stringResource(R.string.pick_system_media))
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    TextButton(
                        onClick = { onSelect(Uri.parse(valueState.value)) }
                    ) {
                        Text(stringResource(R.string.ok))
                    }
                }
            }
        }
    }
}