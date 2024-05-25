package com.example.journeylog.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LocationPicker(address: String?, fetchLocation: () -> Unit) {
    TextButton(onClick = { fetchLocation() }) {
        Icon(Icons.Filled.Place, null)
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(address ?: "Get location")
    }
}
