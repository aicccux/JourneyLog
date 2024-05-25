package com.example.journeylog.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.journeylog.database.Log

@Composable
fun LogCard(modifier: Modifier, log: Log, formattedDate: String, onDelete: (log: Log) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .height(80.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "Title")
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}