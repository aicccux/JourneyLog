package com.example.journeylog.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

@Composable
fun EmptyLogMessage(modifier: Modifier) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Hi there \uD83D\uDC4B",
            style = MaterialTheme.typography.headlineMedium,
            fontFamily = FontFamily.Serif
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "Create a log by clicking the ✚ icon below \uD83D\uDC47",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}