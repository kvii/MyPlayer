package com.example.myplayer.ui.component

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.myplayer.R

@Composable
fun MinuteField(
    minute: Int?,
    readOnly: Boolean,
    enabled: Boolean,
    onChange: (Int?) -> Unit,
) {
    TextField(
        value = minute?.toString() ?: "",
        readOnly = readOnly,
        onValueChange = { onChange(it.toIntOrNull()) },
        suffix = { Text(stringResource(R.string.minute)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
        ),
        singleLine = true,
        enabled = enabled,
    )
}