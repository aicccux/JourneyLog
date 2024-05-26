package com.example.journeylog.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.journeylog.database.Log

@Composable
fun LogCard(modifier: Modifier, log: Log, formattedDate: String, onDelete: (log: Log) -> Unit) {
    Card(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Row(Modifier.padding(8.dp, 0.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = formattedDate,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.headlineSmall
            )
            IconButton(onClick = { onDelete(log) }) {
                Icon(
                    imageVector = Icons.Filled.Delete, contentDescription = "Delete log"
                )
            }
        }
        Row(Modifier.padding(8.dp, 0.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Place, null)
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(log.place)
        }
        PhotoGrid(Modifier.padding(16.dp), photos = log.photos)
    }
    Spacer(modifier = Modifier.height(8.dp))
}